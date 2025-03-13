package se.sundsvall.oepintegrator.service;

import static org.zalando.problem.Status.NOT_FOUND;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.Instance;
import se.sundsvall.oepintegrator.integration.db.InstanceRepository;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import se.sundsvall.oepintegrator.service.mapper.InstanceMapper;
import se.sundsvall.oepintegrator.utility.EncryptionUtility;

@Service
public class InstanceService {

	private static final String ENTITY_NOT_FOUND = "An instance with id '%s' could not be found in municipality with id '%s'";

	private final InstanceRepository instanceRepository;

	private final EncryptionUtility encryptionUtility;

	private final OpeneClientFactory clientFactory;

	public InstanceService(final InstanceRepository instanceRepository, final EncryptionUtility encryptionUtility, final OpeneClientFactory clientFactory) {
		this.instanceRepository = instanceRepository;
		this.encryptionUtility = encryptionUtility;
		this.clientFactory = clientFactory;
	}

	public List<Instance> getInstances(final String municipalityId) {
		return InstanceMapper.toInstances(instanceRepository.findByMunicipalityId(municipalityId));
	}

	public Instance getInstance(final String municipalityId, final String instanceId) {
		return InstanceMapper.toInstance(getInstanceEntity(municipalityId, instanceId));

	}

	@Transactional
	public String createInstance(final String municipalityId, final Instance instance) {
		final var encryptedPassword = encryptionUtility.encrypt(instance.getPassword().getBytes());
		final var result = instanceRepository.save(InstanceMapper.fromInstance(municipalityId, instance, encryptedPassword));
		clientFactory.createClient(result);
		return result.getId();
	}

	@Transactional
	public void updateInstance(final String municipalityId, final String id, final Instance instance) {
		final var entity = getInstanceEntity(municipalityId, id);

		String encryptedPassword = null;
		if (instance.getPassword() != null) {
			encryptedPassword = encryptionUtility.encrypt(instance.getPassword().getBytes());
		}

		instanceRepository.save(InstanceMapper.updateInstance(entity, instance, encryptedPassword));
		clientFactory.removeClient(municipalityId, instance.getInstanceType());
		clientFactory.createClient(entity);
	}

	@Transactional
	public void deleteInstance(final String municipalityId, final String instanceId) {
		final var entity = getInstanceEntity(municipalityId, instanceId);
		instanceRepository.deleteById(instanceId);
		clientFactory.removeClient(municipalityId, entity.getInstanceType());
	}

	private InstanceEntity getInstanceEntity(final String municipalityId, final String instanceId) {
		return instanceRepository.findByMunicipalityIdAndId(municipalityId, instanceId).orElseThrow(
			() -> Problem.valueOf(NOT_FOUND, ENTITY_NOT_FOUND.formatted(instanceId, municipalityId)));
	}

}
