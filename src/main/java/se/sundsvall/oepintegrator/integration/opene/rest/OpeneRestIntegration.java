package se.sundsvall.oepintegrator.integration.opene.rest;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Optional.ofNullable;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.oepintegrator.integration.opene.soap.model.message.WebmessageMapper.toWebmessages;
import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toCaseEnvelopeList;
import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toCaseStatus;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

/**
 * Integration class for Open-E REST API. This will be used to call the Open-E REST API like this:
 *
 * <pre>
 * {@code
 * public void doStuff(final String municipalityId, final String instanceId) {
 *     LOG.info("Example of calling OpenE REST client");
 *     final var client = clientFactory.getRestClient(municipalityId, instanceId);
 *     client.doCall();
 * }}
 */
@Component
public class OpeneRestIntegration {

	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	private final OpeneClientFactory clientFactory;

	public OpeneRestIntegration(final OpeneClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	public List<Webmessage> getWebmessagesByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		final var messages = client.getWebmessagesByFamilyId(familyId, fromDateTime.format(OPEN_E_DATE_TIME_FORMAT), toDateTime.format(OPEN_E_DATE_TIME_FORMAT));
		return toWebmessages(municipalityId, messages, familyId, instanceType);
	}

	public List<Webmessage> getWebmessagesByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		final var messages = client.getWebmessagesByFlowInstanceId(flowInstanceId, fromDateTime.format(OPEN_E_DATE_TIME_FORMAT), toDateTime.format(OPEN_E_DATE_TIME_FORMAT));
		return toWebmessages(municipalityId, messages, instanceType);
	}

	public ResponseEntity<InputStreamResource> getAttachmentById(final String municipalityId, final InstanceType instanceType, final Integer attachmentId) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return validateResponse(client.getAttachmentById(attachmentId), "Failed to get attachment by ID");
	}

	public List<CaseEnvelope> getCaseListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseEnvelopeList(client.getCaseListByFamilyId(familyId, status, formatLocalDate(fromDate), formatLocalDate(toDate)).orElse(EMPTY_BYTE_ARRAY));
	}

	public List<CaseEnvelope> getCaseListByCitizenIdentifier(final String municipalityId, final InstanceType instanceType, final String legalId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseEnvelopeList(client.getCaseListByCitizenIdentifier(legalId, status, formatLocalDate(fromDate), formatLocalDate(toDate)).orElse(EMPTY_BYTE_ARRAY));
	}

	public CaseStatus getCaseStatusByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseStatus(client.getCaseStatusByFlowInstanceId(flowInstanceId).orElseThrow(() -> Problem.valueOf(NOT_FOUND, "No status found for flow instance ID: '%s'".formatted(flowInstanceId))));
	}

	public ResponseEntity<InputStreamResource> getCasePdfByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return validateResponse(client.getCasePdfByFlowInstanceId(flowInstanceId), "Failed to get case PDF by flow instance ID");
	}

	public ResponseEntity<InputStreamResource> getCaseAttachment(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final String queryId, final String fileId) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return validateResponse(client.getCaseAttachment(flowInstanceId, queryId, fileId), "Failed to get case attachment");
	}

	private String formatLocalDate(final LocalDate localDate) {
		return ofNullable(localDate).map(date -> date.format(ISO_LOCAL_DATE)).orElse(null);
	}

	private <T> ResponseEntity<T> validateResponse(final ResponseEntity<T> response, final String errorMessage) {
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw Problem.valueOf(Status.valueOf(response.getStatusCode().value()), errorMessage);
		}

		return response;
	}
}
