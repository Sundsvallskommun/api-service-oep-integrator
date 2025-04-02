package se.sundsvall.oepintegrator.integration.opene.rest;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Optional.ofNullable;
import static se.sundsvall.oepintegrator.integration.opene.soap.model.message.WebmessageMapper.toWebmessages;
import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toCaseEnvelopeList;
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

		final var responseEntity = client.getAttachmentById(attachmentId);
		validateResponse(responseEntity, "Failed to get attachment by ID");
		return responseEntity;
	}

	public List<CaseEnvelope> getCaseListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseEnvelopeList(client.getCaseListByFamilyId(familyId, status, formatLocalDate(fromDate), formatLocalDate(toDate)).orElse(new byte[0]));
	}

	public List<CaseEnvelope> getCaseListByCitizenIdentifier(final String municipalityId, final InstanceType instanceType, final String legalId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseEnvelopeList(client.getCaseListByCitizenIdentifier(legalId, status, formatLocalDate(fromDate), formatLocalDate(toDate)).orElse(new byte[0]));
	}

	public ResponseEntity<InputStreamResource> getCasePdfByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);

		final var responseEntity = client.getCasePdfByFlowInstanceId(flowInstanceId);
		validateResponse(responseEntity, "Failed to get case PDF by flow instance ID");
		return responseEntity;
	}

	private String formatLocalDate(final LocalDate localDate) {
		return ofNullable(localDate).map(date -> date.format(ISO_LOCAL_DATE)).orElse(null);
	}

	private void validateResponse(final ResponseEntity<InputStreamResource> response, final String errorMessage) {
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw Problem.valueOf(Status.valueOf(response.getStatusCode().value()), errorMessage);
		}
	}

}
