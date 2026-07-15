package se.sundsvall.oepintegrator.service;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.oepintegrator.api.model.cases.Case;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeResponse;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.integration.db.BlackListRepository;
import se.sundsvall.oepintegrator.integration.db.model.BlackListEntity;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.rest.model.MetadataFlow;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.integration.party.PartyClient;
import se.sundsvall.oepintegrator.service.mapper.CaseStatusMapper;
import se.sundsvall.oepintegrator.util.enums.InstanceType;

import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toConfirmDelivery;
import static se.sundsvall.oepintegrator.util.StreamUtils.copyResponseEntityToHttpServletResponse;

@Service
public class CaseService {

	private static final Logger LOG = LoggerFactory.getLogger(CaseService.class);

	private final OpeneSoapIntegration openeSoapIntegration;
	private final OpeneRestIntegration openeRestIntegration;
	private final PartyClient partyClient;
	private final BlackListRepository blackListRepository;

	public CaseService(
		final OpeneSoapIntegration openeSoapIntegration,
		final OpeneRestIntegration openeRestIntegration,
		final PartyClient partyClient,
		final BlackListRepository blackListRepository) {

		this.openeSoapIntegration = openeSoapIntegration;
		this.openeRestIntegration = openeRestIntegration;
		this.partyClient = partyClient;
		this.blackListRepository = blackListRepository;
	}

	public void getCasePdfByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final HttpServletResponse response) {
		final var responseEntity = openeRestIntegration.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
		copyResponseEntityToHttpServletResponse(responseEntity, response, "Unable to get case pdf");
	}

	public void confirmDelivery(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final ConfirmDeliveryRequest request) {
		openeSoapIntegration.confirmDelivery(municipalityId, instanceType, toConfirmDelivery(flowInstanceId, request));
	}

	public CaseStatusChangeResponse setStatusByFlowinstanceId(final String municipalityId, final InstanceType instanceType, final CaseStatusChangeRequest request, final String flowInstanceId) {
		try {
			return new CaseStatusChangeResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, CaseStatusMapper.toSetStatus(request, flowInstanceId)).getEventID());
		} catch (final Exception e) {
			LOG.info("Failed to set status for flow instance ID '{}', case may have been purged in Open-E. Error: {}", flowInstanceId, e.getMessage());
			return new CaseStatusChangeResponse();
		}
	}

	public CaseStatusChangeResponse setStatusByExternalId(final String municipalityId, final InstanceType instanceType, final CaseStatusChangeRequest request, final String externalId, final String system) {
		return new CaseStatusChangeResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, CaseStatusMapper.toSetStatus(request, externalId, system)).getEventID());
	}

	public List<CaseEnvelope> getCaseEnvelopeListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		return openeRestIntegration.getCaseListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate);
	}

	public List<CaseEnvelope> getCaseEnvelopeListByCitizenIdentifier(final String municipalityId, final InstanceType instanceType, final String partyId, final String status, final LocalDate fromDate, final LocalDate toDate, final Boolean includeStatus) {

		final var legalId = resolveLegalId(municipalityId, partyId);
		final var blackListedFamilyIds = loadBlackListedFamilyIds(municipalityId, instanceType);

		final var waiting = enrichAndFilter(municipalityId, instanceType,
			openeRestIntegration.getWaitingCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, status, fromDate, toDate, includeStatus),
			blackListedFamilyIds);

		final var other = enrichAndFilter(municipalityId, instanceType,
			openeRestIntegration.getCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, status, fromDate, toDate, includeStatus),
			blackListedFamilyIds);

		return Stream.concat(waiting.stream(), other.stream()).toList();
	}

	public List<CaseEnvelope> getMultisignCaseEnvelopeListByCitizenIdentifier(final String municipalityId, final InstanceType instanceType, final String partyId, final String status, final LocalDate fromDate, final LocalDate toDate,
		final Boolean includeStatus) {

		final var legalId = resolveLegalId(municipalityId, partyId);

		return enrichAndFilter(municipalityId, instanceType,
			openeRestIntegration.getWaitingCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, status, fromDate, toDate, includeStatus),
			loadBlackListedFamilyIds(municipalityId, instanceType));
	}

	public List<CaseEnvelope> getMultisignCaseEnvelopeListByUserId(final String municipalityId, final InstanceType instanceType, final String userId, final String status, final LocalDate fromDate, final LocalDate toDate,
		final Boolean includeStatus) {

		return enrichAndFilter(municipalityId, instanceType,
			openeRestIntegration.getWaitingCaseListByUserId(municipalityId, instanceType, userId, status, fromDate, toDate, includeStatus),
			loadBlackListedFamilyIds(municipalityId, instanceType));
	}

	public List<CaseEnvelope> getUnsubmittedCaseEnvelopeListByCitizenIdentifier(final String municipalityId, final InstanceType instanceType, final String partyId, final Boolean includeStatus) {

		final var legalId = resolveLegalId(municipalityId, partyId);

		return enrichAndFilter(municipalityId, instanceType,
			openeRestIntegration.getUnsubmittedCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, includeStatus),
			loadBlackListedFamilyIds(municipalityId, instanceType));
	}

	public List<CaseEnvelope> getUnsubmittedCaseEnvelopeListByUserId(final String municipalityId, final InstanceType instanceType, final String userId, final Boolean includeStatus) {

		return enrichAndFilter(municipalityId, instanceType,
			openeRestIntegration.getUnsubmittedCaseListByUserId(municipalityId, instanceType, userId, includeStatus),
			loadBlackListedFamilyIds(municipalityId, instanceType));
	}

	private String resolveLegalId(final String municipalityId, final String partyId) {
		return partyClient.getLegalId(municipalityId, PRIVATE, partyId)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, "Citizen identifier not found for partyId: %s".formatted(partyId)));
	}

	private Set<String> loadBlackListedFamilyIds(final String municipalityId, final InstanceType instanceType) {
		return blackListRepository.findByMunicipalityIdAndInstanceType(municipalityId, instanceType).stream()
			.map(BlackListEntity::getFamilyId)
			.collect(toSet());
	}

	private List<CaseEnvelope> enrichAndFilter(final String municipalityId, final InstanceType instanceType, final List<CaseEnvelope> source, final Set<String> blackListedFamilyIds) {
		return source.stream()
			.filter(not(envelope -> blackListedFamilyIds.contains(envelope.getFamilyId())))
			.map(envelope -> envelope.withDisplayName(getDisplayName(municipalityId, instanceType, envelope.getFamilyId())))
			.toList();
	}

	public CaseStatus getCaseStatusByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId) {
		return openeRestIntegration.getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
	}

	public void getCaseAttachment(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final String queryId, final String fileId, final HttpServletResponse response) {
		final var responseEntity = openeRestIntegration.getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId);
		copyResponseEntityToHttpServletResponse(responseEntity, response, "Unable to get case attachment");
	}

	public Case getCaseByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId) {
		return openeRestIntegration.getCaseByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
	}

	String getDisplayName(final String municipalityId, final InstanceType instanceType, final String flowFamilyId) {
		return Optional.ofNullable(findDisplayName(openeRestIntegration.getRestrictedMetadata(municipalityId, instanceType), flowFamilyId))
			.orElseGet(() -> findDisplayName(openeRestIntegration.getMetadata(municipalityId, instanceType), flowFamilyId));
	}

	private String findDisplayName(final List<MetadataFlow> flows, final String flowFamilyId) {
		return flows.stream()
			.filter(metadataFlow -> flowFamilyId.equalsIgnoreCase(metadataFlow.flowFamilyId()))
			.findFirst()
			.map(MetadataFlow::displayName)
			.orElse(null);
	}
}
