package se.sundsvall.oepintegrator.integration.opene;

import static java.util.Optional.ofNullable;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.integration.db.InstanceRepository;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.db.model.enums.IntegrationType;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestClient;
import se.sundsvall.oepintegrator.integration.opene.rest.configuration.OpeneRestClientFactory;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapClient;
import se.sundsvall.oepintegrator.integration.opene.soap.configuration.SoapClientFactory;

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

	private final Map<ClientKey, OpeneClient> clients = new HashMap<>();
	private final OpeneRestClientFactory restClientFactory;
	private final SoapClientFactory soapClientFactory;
	private final InstanceRepository instanceRepository;

	OpeneClientFactory(final InstanceRepository instanceRepository, final OpeneRestClientFactory restClientFactory, final SoapClientFactory soapClientFactory) {
		this.restClientFactory = restClientFactory;
		this.soapClientFactory = soapClientFactory;
		this.instanceRepository = instanceRepository;
		init();
	}

	/**
	 * Initialize the OpenE clients by creating clients for all instances in the database. This method is called when the
	 * application starts.
	 */
	private void init() {
		instanceRepository.findAll().forEach(this::createClient);
	}

	/**
	 * Get the OpenE REST client for the given municipalityId and instanceId.
	 *
	 * @param  municipalityId the municipalityId
	 * @param  instanceType   the instanceType
	 * @return                the OpenE REST client
	 */
	public OpeneRestClient getRestClient(final String municipalityId, final InstanceType instanceType) {
		return ofNullable(clients.get(new ClientKey(municipalityId, instanceType, IntegrationType.REST)))
			.filter(OpeneRestClient.class::isInstance)
			.map(OpeneRestClient.class::cast)
			.orElseThrow(() -> Problem.valueOf(INTERNAL_SERVER_ERROR, String.format("No %s OpenE REST client exists for municipalityId %s", instanceType, municipalityId)));
	}

	/**
	 * Get the OpenE SOAP client for the given municipalityId and instance.
	 *
	 * @param  municipalityId the municipalityId
	 * @param  instanceType   the instanceType
	 * @return                the OpenE SOAP client
	 */
	public OpeneSoapClient getSoapClient(final String municipalityId, final InstanceType instanceType) {
		return ofNullable(clients.get(new ClientKey(municipalityId, instanceType, IntegrationType.SOAP)))
			.filter(OpeneSoapClient.class::isInstance)
			.map(OpeneSoapClient.class::cast)
			.orElseThrow(() -> Problem.valueOf(INTERNAL_SERVER_ERROR, String.format("No %s OpenE SOAP client exists for municipalityId %s", instanceType, municipalityId)));
	}

	/**
	 * Remove the OpenE client for the given municipalityId and instanceId.
	 *
	 * @param municipalityId the municipalityId
	 * @param instanceType   the instanceType
	 */
	public void removeClient(final String municipalityId, final InstanceType instanceType, final IntegrationType integrationType) {
		clients.remove(new ClientKey(municipalityId, instanceType, integrationType));
	}

	/**
	 * Create the OpenE client for the given instanceEntity. The client is created based on the integrationType of the
	 * instanceEntity.
	 *
	 * @param instanceEntity the instanceEntity
	 */
	public void createClient(final InstanceEntity instanceEntity) {
		switch (instanceEntity.getIntegrationType()) {
			case REST -> clients.put(new ClientKey(instanceEntity), restClientFactory.createRestClient(instanceEntity));
			case SOAP -> clients.put(new ClientKey(instanceEntity), soapClientFactory.createSoapClient(instanceEntity));
			default -> throw new IllegalStateException("Unexpected IntegrationType: " + instanceEntity.getIntegrationType());
		}
	}

	private record ClientKey(String municipalityId, InstanceType instanceType, IntegrationType integrationType) {
		ClientKey(final InstanceEntity instanceEntity) {
			this(instanceEntity.getMunicipalityId(), instanceEntity.getInstanceType(), instanceEntity.getIntegrationType());
		}
	}

}
