package se.sundsvall.oepintegrator.service.mapper;

import java.util.List;
import java.util.Optional;
import se.sundsvall.oepintegrator.api.model.instance.Instance;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;

public final class InstanceMapper {

	private InstanceMapper() {}

	public static List<Instance> toInstances(final List<InstanceEntity> entities) {
		return entities.stream()
			.map(InstanceMapper::toInstance)
			.toList();
	}

	public static Instance toInstance(final InstanceEntity entity) {
		return Instance.create()
			.withId(entity.getId())
			.withIntegrationType(entity.getIntegrationType())
			.withInstanceType(entity.getInstanceType())
			.withBaseUrl(entity.getBaseUrl())
			.withUsername(entity.getUsername())
			.withFamilyIds(entity.getFamilyIds())
			.withConnectTimeout(entity.getConnectTimeout())
			.withReadTimeout(entity.getReadTimeout());
	}

	public static InstanceEntity fromInstance(final String municipalityId, final Instance instance, final String encryptedPassword) {
		return InstanceEntity.create()
			.withMunicipalityId(municipalityId)
			.withIntegrationType(instance.getIntegrationType())
			.withInstanceType(instance.getInstanceType())
			.withBaseUrl(instance.getBaseUrl())
			.withUsername(instance.getUsername())
			.withFamilyIds(instance.getFamilyIds())
			.withConnectTimeout(Optional.ofNullable(instance.getConnectTimeout()).orElse(5))
			.withReadTimeout(Optional.ofNullable(instance.getReadTimeout()).orElse(60))
			.withPassword(encryptedPassword);
	}

	public static InstanceEntity updateInstance(final InstanceEntity entity, final Instance instance, final String encryptedPassword) {
		Optional.ofNullable(instance.getIntegrationType()).ifPresent(entity::setIntegrationType);
		Optional.ofNullable(instance.getInstanceType()).ifPresent(entity::setInstanceType);
		Optional.ofNullable(instance.getBaseUrl()).ifPresent(entity::setBaseUrl);
		Optional.ofNullable(instance.getUsername()).ifPresent(entity::setUsername);
		Optional.ofNullable(instance.getFamilyIds()).ifPresent(entity::setFamilyIds);
		Optional.ofNullable(instance.getConnectTimeout()).ifPresent(entity::setConnectTimeout);
		Optional.ofNullable(instance.getReadTimeout()).ifPresent(entity::setReadTimeout);
		Optional.ofNullable(encryptedPassword).ifPresent(entity::setPassword);

		return entity;
	}
}
