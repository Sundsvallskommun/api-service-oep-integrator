package se.sundsvall.oepintegrator.integration.opene.rest;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LAST_MODIFIED;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static se.sundsvall.oepintegrator.integration.opene.soap.model.message.WebmessageMapper.toWebmessages;
import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toCaseEnvelopeList;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.zalando.problem.Problem;
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

	public void getAttachmentById(final String municipalityId, final InstanceType instanceType, final Integer attachmentId, final HttpServletResponse response) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);

		final var responseEntity = client.getAttachmentById(attachmentId);
		setResponse(responseEntity, response, "Unable to get attachment");
	}

	public List<CaseEnvelope> getCaseListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseEnvelopeList(client.getCaseListByFamilyId(familyId, status, formatLocalDate(fromDate), formatLocalDate(toDate)).orElse(new byte[0]));
	}

	public void getCasePdfByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final HttpServletResponse response) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);

		final var responseEntity = client.getCasePdfByFlowInstanceId(flowInstanceId);
		setResponse(responseEntity, response, "Unable to get case pdf");
	}

	private String formatLocalDate(final LocalDate localDate) {
		return ofNullable(localDate).map(date -> date.format(ISO_LOCAL_DATE)).orElse(null);
	}

	private void setResponse(final ResponseEntity<InputStreamResource> responseEntity, final HttpServletResponse response, final String errorMessage) {
		try (final var inputStream = Objects.requireNonNull(responseEntity.getBody()).getInputStream();
			final var outputStream = response.getOutputStream()) {

			final var headerSet = Set.of(LAST_MODIFIED, CONTENT_TYPE, CONTENT_DISPOSITION, CONTENT_LENGTH);
			headerSet.forEach(header -> responseEntity.getHeaders()
				.getOrDefault(header, List.of())
				.stream()
				.findFirst().ifPresent(value -> response.setHeader(header, value)));

			StreamUtils.copy(inputStream, outputStream);
		} catch (final IOException e) {
			throw Problem.valueOf(INTERNAL_SERVER_ERROR, errorMessage);
		}
	}
}
