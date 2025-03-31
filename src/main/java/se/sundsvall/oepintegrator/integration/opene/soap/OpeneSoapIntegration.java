package se.sundsvall.oepintegrator.integration.opene.soap;

import callback.AddMessage;
import callback.AddMessageAsOwner;
import callback.AddMessageAsOwnerResponse;
import callback.AddMessageResponse;
import callback.SetStatus;
import callback.SetStatusResponse;
import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

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

	public AddMessageAsOwnerResponse addMessageAsOwner(final String municipalityId, final InstanceType instanceType, final AddMessageAsOwner addMessageAsOwner) {
		final var client = clientFactory.getSoapClient(municipalityId, instanceType);
		return client.addMessageAsOwner(addMessageAsOwner);
	}

	public SetStatusResponse setStatus(final String municipalityId, final InstanceType instanceType, final SetStatus setStatus) {
		final var client = clientFactory.getSoapClient(municipalityId, instanceType);
		return client.setStatus(setStatus);
	}
}
