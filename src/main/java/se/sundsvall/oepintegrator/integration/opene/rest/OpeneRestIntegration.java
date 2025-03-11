package se.sundsvall.oepintegrator.integration.opene.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;

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

	private static final Logger LOG = LoggerFactory.getLogger(OpeneRestIntegration.class);

	private final OpeneClientFactory clientFactory;

	public OpeneRestIntegration(final OpeneClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

}
