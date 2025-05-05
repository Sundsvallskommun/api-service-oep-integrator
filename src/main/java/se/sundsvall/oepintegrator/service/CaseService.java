package se.sundsvall.oepintegrator.service;

import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toConfirmDelivery;
import static se.sundsvall.oepintegrator.util.StreamUtils.copyResponseEntityToHttpServletResponse;

import generated.se.sundsvall.party.PartyType;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.oepintegrator.api.model.cases.Case;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeResponse;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.integration.party.PartyClient;
import se.sundsvall.oepintegrator.service.mapper.CaseStatusMapper;
import se.sundsvall.oepintegrator.util.enums.InstanceType;

@Service
public class CaseService {

	private final OpeneSoapIntegration openeSoapIntegration;
	private final OpeneRestIntegration openeRestIntegration;
	private final PartyClient partyClient;

	public CaseService(final OpeneSoapIntegration openeSoapIntegration, final OpeneRestIntegration openeRestIntegration, final PartyClient partyClient) {
		this.openeSoapIntegration = openeSoapIntegration;
		this.openeRestIntegration = openeRestIntegration;
		this.partyClient = partyClient;
	}

	public void getCasePdfByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final HttpServletResponse response) {
		final var responseEntity = openeRestIntegration.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
		copyResponseEntityToHttpServletResponse(responseEntity, response, "Unable to get case pdf");
	}

	public void confirmDelivery(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final ConfirmDeliveryRequest request) {
		openeSoapIntegration.confirmDelivery(municipalityId, instanceType, toConfirmDelivery(flowInstanceId, request));
	}

	public CaseStatusChangeResponse setStatusByFlowinstanceId(final String municipalityId, final InstanceType instanceType, final CaseStatusChangeRequest request, final String flowInstanceId) {
		return new CaseStatusChangeResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, CaseStatusMapper.toSetStatus(request, flowInstanceId)).getEventID());
	}

	public CaseStatusChangeResponse setStatusByExternalId(final String municipalityId, final InstanceType instanceType, final CaseStatusChangeRequest request, final String externalId, final String system) {
		return new CaseStatusChangeResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, CaseStatusMapper.toSetStatus(request, externalId, system)).getEventID());
	}

	public List<CaseEnvelope> getCaseEnvelopeListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		return openeRestIntegration.getCaseListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate);
	}

	public List<CaseEnvelope> getCaseEnvelopeListByCitizenIdentifier(final String municipalityId, final InstanceType instanceType, final String partyId, final String status, final LocalDate fromDate, final LocalDate toDate) {

		final var legalId = partyClient.getLegalId(municipalityId, PartyType.PRIVATE, partyId)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, "Citizen identifier not found for partyId: %s".formatted(partyId)));

		return openeRestIntegration.getCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, status, fromDate, toDate);
	}

	public CaseStatus getCaseStatusByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId) {
		return openeRestIntegration.getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
	}

	public void getCaseAttachment(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final String queryId, final String fileId, final HttpServletResponse response) {
		final var responseEntity = openeRestIntegration.getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId);
		copyResponseEntityToHttpServletResponse(responseEntity, response, "Unable to get case attachment");
	}

	public Case getCaseByFlowInstanceId(@ValidMunicipalityId final String municipalityId, final InstanceType instanceType, final String flowInstanceId) {
		return null;
	}
}
