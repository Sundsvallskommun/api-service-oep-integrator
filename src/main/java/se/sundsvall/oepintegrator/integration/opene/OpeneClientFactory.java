package se.sundsvall.oepintegrator.integration.opene;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPEncoder;
import feign.soap.SOAPErrorDecoder;
import jakarta.xml.soap.SOAPConstants;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.integration.db.InstanceRepository;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestClient;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapClient;
import se.sundsvall.oepintegrator.utility.EncryptionUtility;

/**
 * Factory class for creating OpenE clients. This class is responsible for creating and managing OpenE clients.
 *
 * <p>
 * It creates clients for both REST and SOAP integrations and stores them in a map for easy access.
 * </p>
 * <p>
 * It also provides methods for getting clients based on municipalityId and instanceId.
 * </p>
 */
@Component
public class OpeneClientFactory {

	private static final Logger LOG = LoggerFactory.getLogger(OpeneClientFactory.class);

	private static final JAXBContextFactory JAXB_FACTORY = new JAXBContextFactory.Builder()
		.withMarshallerJAXBEncoding(StandardCharsets.UTF_8.toString())
		.build();

	private static final SOAPEncoder.Builder ENCODER_BUILDER = new SOAPEncoder.Builder()
		.withCharsetEncoding(StandardCharsets.UTF_8)
		.withFormattedOutput(false)
		.withJAXBContextFactory(JAXB_FACTORY)
		.withSOAPProtocol(SOAPConstants.SOAP_1_1_PROTOCOL)
		.withWriteXmlDeclaration(true);

	private final Map<ClientKey, OpeneClient> clients = new HashMap<>();
	private final EncryptionUtility encryptionUtility;
	private final ApplicationContext applicationContext;

	OpeneClientFactory(final InstanceRepository instanceRepository, final EncryptionUtility encryptionUtility, final ApplicationContext applicationContext) {
		this.encryptionUtility = encryptionUtility;
		this.applicationContext = applicationContext;

		instanceRepository.findAll().forEach(this::createClient);
	}

	/**
	 * Get the OpenE REST client for the given municipalityId and instanceId.
	 *
	 * @param  municipalityId the municipalityId
	 * @param  instanceId     the instanceId
	 * @return                the OpenE REST client
	 */
	public OpeneRestClient getRestClient(final String municipalityId, final String instanceId) {
		return ofNullable(clients.get(new ClientKey(municipalityId, instanceId)))
			.filter(OpeneRestClient.class::isInstance)
			.map(OpeneRestClient.class::cast)
			.orElseThrow(() -> Problem.valueOf(INTERNAL_SERVER_ERROR, String.format("No OpenE REST client with id: %s exists for municipalityId %s", instanceId, municipalityId)));
	}

	/**
	 * Get the OpenE SOAP client for the given municipalityId and instanceId.
	 *
	 * @param  municipalityId the municipalityId
	 * @param  instanceId     the instanceId
	 * @return                the OpenE SOAP client
	 */
	public OpeneSoapClient getSoapClient(final String municipalityId, final String instanceId) {
		return ofNullable(clients.get(new ClientKey(municipalityId, instanceId)))
			.filter(OpeneSoapClient.class::isInstance)
			.map(OpeneSoapClient.class::cast)
			.orElseThrow(() -> Problem.valueOf(INTERNAL_SERVER_ERROR, String.format("No OpenE SOAP client with id: %s exists for municipalityId %s", instanceId, municipalityId)));
	}

	/**
	 * Get the OpenE client for the given municipalityId and instanceId.
	 *
	 * @param  municipalityId the municipalityId
	 * @param  instanceId     the instanceId
	 * @return                the OpenE client
	 */
	public OpeneClient getClient(final String municipalityId, final String instanceId) {
		return ofNullable(clients.get(new ClientKey(municipalityId, instanceId)))
			.orElseThrow(() -> Problem.valueOf(INTERNAL_SERVER_ERROR, String.format("No OpenE client with id: %s exists for municipalityId %s", instanceId, municipalityId)));
	}

	/**
	 * Remove the OpenE client for the given municipalityId and instanceId.
	 *
	 * @param municipalityId the municipalityId
	 * @param instanceId     the instanceId
	 */
	public void removeClient(final String municipalityId, final String instanceId) {
		clients.remove(new ClientKey(municipalityId, instanceId));
	}

	/**
	 * Create the OpenE client for the given instanceEntity. The client is created based on the integrationType of the
	 * instanceEntity.
	 *
	 * @param instanceEntity the instanceEntity
	 */
	public void createClient(final InstanceEntity instanceEntity) {
		switch (instanceEntity.getIntegrationType()) {
			case REST -> createRestClient(instanceEntity);
			case SOAP -> createSoapClient(instanceEntity);
			default -> throw new IllegalStateException("Unexpected IntegrationType: " + instanceEntity.getIntegrationType());
		}
	}

	private void createRestClient(final InstanceEntity instanceEntity) {

		final var clientName = "oep-%s-%s".formatted(instanceEntity.getInstanceType().name().toLowerCase(), instanceEntity.getMunicipalityId());

		final var client = new FeignClientBuilder(applicationContext)
			.forType(OpeneRestClient.class, clientName)
			.customize(builder -> builder
				.errorDecoder(new SOAPErrorDecoder())
				.requestInterceptor(new BasicAuthRequestInterceptor(instanceEntity.getUsername(), encryptionUtility.decrypt(instanceEntity.getPassword())))
				.options(new Request.Options(instanceEntity.getConnectTimeout(), SECONDS, instanceEntity.getReadTimeout(), SECONDS, true)))
			.url(instanceEntity.getBaseUrl())
			.build();
		clients.put(new ClientKey(instanceEntity.getMunicipalityId(), instanceEntity.getId()), client);

		LOG.info("Created OpenE REST client with id {} for municipalityId {} ({})", instanceEntity.getInstanceType(), instanceEntity.getMunicipalityId(), clientName);
	}

	private void createSoapClient(final InstanceEntity environment) {

		final var clientName = "oep-%s-%s".formatted(environment.getInstanceType().name().toLowerCase(), environment.getMunicipalityId());

		final var client = new FeignClientBuilder(applicationContext)
			.forType(OpeneSoapClient.class, clientName)
			.customize(builder -> builder
				.encoder(ENCODER_BUILDER.build())
				.errorDecoder(new SOAPErrorDecoder())
				.requestInterceptor(new BasicAuthRequestInterceptor(environment.getUsername(), encryptionUtility.decrypt(environment.getPassword())))
				.options(new Request.Options(environment.getConnectTimeout(), SECONDS, environment.getReadTimeout(), SECONDS, true)))
			.url(environment.getBaseUrl())
			.build();
		clients.put(new ClientKey(environment.getMunicipalityId(), environment.getId()), client);

		LOG.info("Created OpenE SOAP client with id {} for municipalityId {} ({})", environment.getInstanceType(), environment.getMunicipalityId(), clientName);
	}

	private record ClientKey(String municipalityId, String instanceId) {
	}

}
