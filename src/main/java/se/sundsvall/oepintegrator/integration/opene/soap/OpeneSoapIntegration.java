package se.sundsvall.oepintegrator.integration.opene.soap;

import static se.sundsvall.oepintegrator.integration.opene.soap.model.message.WebmessageMapper.toWebmessages;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import callback.AddMessage;
import callback.AddMessageResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;

/**
 * Integration class for Open-E SOAP API. This will be used to call the Open-E SOAP API like this:
 *
 * <pre>
 * {@code
 * public void doStuff(final String municipalityId, final String instanceId) {
 *     LOG.info("Example of calling OpenE SOAP client");
 *     final var client = clientFactory.getSoapClient(municipalityId, instanceId);
 *     client.doCall();
 * }}
 */
@Component
public class OpeneSoapIntegration {

	private final OpeneClientFactory clientFactory;

	public OpeneSoapIntegration(final OpeneClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	public AddMessageResponse addMessage(final String municipalityId, final InstanceType instanceType, final AddMessage addMessage) {
		final var client = clientFactory.getSoapClient(municipalityId, instanceType);
		return client.addMessage(addMessage);
	}

	public List<Webmessage> getWebmessagesByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		final var client = clientFactory.getSoapClient(municipalityId, instanceType);
		final var messages = client.getWebmessagesByFamilyId(familyId, fromDateTime.format(OPEN_E_DATE_TIME_FORMAT), toDateTime.format(OPEN_E_DATE_TIME_FORMAT));
		return toWebmessages(municipalityId, messages, familyId, instanceType);
	}

	public List<Webmessage> getWebmessagesByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		final var client = clientFactory.getSoapClient(municipalityId, instanceType);
		final var messages = client.getWebmessagesByFlowInstanceId(flowInstanceId, fromDateTime.format(OPEN_E_DATE_TIME_FORMAT), toDateTime.format(OPEN_E_DATE_TIME_FORMAT));
		return toWebmessages(municipalityId, messages, instanceType);
	}
}
