package se.sundsvall.oepintegrator.integration.opene.soap;

import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import callback.AddMessage;
import callback.AddMessageResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import se.sundsvall.oepintegrator.integration.opene.soap.model.message.WebmessageMapper;

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

	public List<Webmessage> getMessages(final String municipalityId, final InstanceType instanceType, final String familyId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		final var client = clientFactory.getSoapClient(municipalityId, instanceType);
		return WebmessageMapper.toWebmessages(municipalityId, client.getMessages(familyId, fromDateTime.format(OPEN_E_DATE_TIME_FORMAT), toDateTime.format(OPEN_E_DATE_TIME_FORMAT)), familyId, instanceType);
	}

}
