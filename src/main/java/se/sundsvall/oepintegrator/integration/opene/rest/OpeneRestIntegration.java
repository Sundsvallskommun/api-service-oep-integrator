package se.sundsvall.oepintegrator.integration.opene.rest;

import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageAttachmentData;
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

	public WebmessageAttachmentData getAttachmentById(final String municipalityId, final InstanceType instanceType, final Integer attachmentId) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		final var data = client.getAttachmentById(attachmentId);
		return toWebmessageAttachmentData(data);
	}

	public List<CaseEnvelope> getCaseListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseEnvelopeList(client.getCaseListByFamilyId(familyId, status, fromDate, toDate).orElse(new byte[0]));
	}
}
