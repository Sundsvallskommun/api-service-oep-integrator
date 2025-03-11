package se.sundsvall.oepintegrator.integration.opene.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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

	private static final Logger LOG = LoggerFactory.getLogger(OpeneSoapIntegration.class);

	private final OpeneClientFactory clientFactory;

	public OpeneSoapIntegration(final OpeneClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

}
