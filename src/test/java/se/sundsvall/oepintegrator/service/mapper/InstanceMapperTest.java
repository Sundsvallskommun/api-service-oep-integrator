package se.sundsvall.oepintegrator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.INTERNAL;
import static se.sundsvall.oepintegrator.utility.enums.IntegrationType.REST;
import static se.sundsvall.oepintegrator.utility.enums.IntegrationType.SOAP;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.sundsvall.oepintegrator.api.model.Instance;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;

class InstanceMapperTest {

	@Test
	void toInstances() {
		// Arrange
		final var integrationType1 = REST;
		final var instanceType1 = INTERNAL;
		final var baseUrl1 = "url1";
		final var username1 = "user1";
		final var familyIds1 = List.of("id1");
		final var connectTimeout1 = 10;
		final var readTimeout1 = 20;

		final var integrationType2 = SOAP;
		final var instanceType2 = EXTERNAL;
		final var baseUrl2 = "url2";
		final var username2 = "user2";
		final var familyIds2 = List.of("id2");
		final var connectTimeout2 = 30;
		final var readTimeout2 = 40;

		final var entity1 = new InstanceEntity()
			.withIntegrationType(integrationType1)
			.withInstanceType(instanceType1)
			.withBaseUrl(baseUrl1)
			.withUsername(username1)
			.withFamilyIds(familyIds1)
			.withConnectTimeout(connectTimeout1)
			.withReadTimeout(readTimeout1);

		final var entity2 = new InstanceEntity()
			.withIntegrationType(integrationType2)
			.withInstanceType(instanceType2)
			.withBaseUrl(baseUrl2)
			.withUsername(username2)
			.withFamilyIds(familyIds2)
			.withConnectTimeout(connectTimeout2)
			.withReadTimeout(readTimeout2);

		final var entities = List.of(entity1, entity2);

		// Act
		final var instances = InstanceMapper.toInstances(entities);

		// Assert
		assertThat(instances).isNotNull().hasSize(2);
		assertThat(instances.getFirst().getIntegrationType()).isEqualTo(integrationType1);
		assertThat(instances.getFirst().getInstanceType()).isEqualTo(instanceType1);
		assertThat(instances.getLast().getIntegrationType()).isEqualTo(integrationType2);
		assertThat(instances.getLast().getInstanceType()).isEqualTo(instanceType2);
	}

	@Test
	void toInstance() {
		// Arrange
		final var integrationType = SOAP;
		final var instanceType = EXTERNAL;
		final var baseUrl = "url";
		final var username = "user";
		final var familyIds = List.of("id");
		final var connectTimeout = 10;
		final var readTimeout = 20;

		final var entity = new InstanceEntity()
			.withIntegrationType(integrationType)
			.withInstanceType(instanceType)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		// Act
		final var instance = InstanceMapper.toInstance(entity);

		// Assert
		assertThat(instance).isNotNull();
		assertThat(instance.getIntegrationType()).isEqualTo(integrationType);
		assertThat(instance.getInstanceType()).isEqualTo(instanceType);
		assertThat(instance.getBaseUrl()).isEqualTo(baseUrl);
		assertThat(instance.getUsername()).isEqualTo(username);
		assertThat(instance.getFamilyIds()).isEqualTo(familyIds);
		assertThat(instance.getConnectTimeout()).isEqualTo(connectTimeout);
		assertThat(instance.getReadTimeout()).isEqualTo(readTimeout);
	}

	@Test
	void fromInstance() {
		// Arrange
		final var municipalityId = "municipalityId";
		final var integrationType = REST;
		final var instanceType = INTERNAL;
		final var baseUrl = "url";
		final var username = "user";
		final var familyIds = List.of("id");
		final var connectTimeout = 10;
		final var readTimeout = 20;
		final var encryptedPassword = "encryptedPassword";

		final var instance = Instance.create()
			.withIntegrationType(integrationType)
			.withInstanceType(instanceType)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		// Act
		final var entity = InstanceMapper.fromInstance(municipalityId, instance, encryptedPassword);

		// Assert
		assertThat(entity).isNotNull();
		assertThat(entity.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(entity.getIntegrationType()).isEqualTo(integrationType);
		assertThat(entity.getInstanceType()).isEqualTo(instanceType);
		assertThat(entity.getBaseUrl()).isEqualTo(baseUrl);
		assertThat(entity.getUsername()).isEqualTo(username);
		assertThat(entity.getFamilyIds()).isEqualTo(familyIds);
		assertThat(entity.getConnectTimeout()).isEqualTo(connectTimeout);
		assertThat(entity.getReadTimeout()).isEqualTo(readTimeout);
		assertThat(entity.getPassword()).isEqualTo(encryptedPassword);
	}

	@Test
	void updateInstance() {
		// Arrange
		final var oldBaseUrl = "oldUrl";
		final var oldUsername = "oldUser";
		final var oldFamilyIds = List.of("oldId");
		final var oldConnectTimeout = 5;
		final var oldReadTimeout = 10;
		final var oldPassword = "oldPassword";

		final var newIntegrationType = REST;
		final var newInstanceType = EXTERNAL;
		final var newBaseUrl = "newUrl";
		final var newUsername = "newUser";
		final var newFamilyIds = List.of("newId");
		final var newConnectTimeout = 15;
		final var newReadTimeout = 25;
		final var newPassword = "newPassword";

		final var entity = new InstanceEntity()
			.withIntegrationType(SOAP)
			.withInstanceType(INTERNAL)
			.withBaseUrl(oldBaseUrl)
			.withUsername(oldUsername)
			.withFamilyIds(oldFamilyIds)
			.withConnectTimeout(oldConnectTimeout)
			.withReadTimeout(oldReadTimeout)
			.withPassword(oldPassword);

		final var instance = Instance.create()
			.withIntegrationType(newIntegrationType)
			.withInstanceType(newInstanceType)
			.withBaseUrl(newBaseUrl)
			.withUsername(newUsername)
			.withFamilyIds(newFamilyIds)
			.withConnectTimeout(newConnectTimeout)
			.withReadTimeout(newReadTimeout);

		// Act
		final var updatedEntity = InstanceMapper.updateInstance(entity, instance, newPassword);

		// Assert
		assertThat(updatedEntity).isNotNull();
		assertThat(updatedEntity.getIntegrationType()).isEqualTo(newIntegrationType);
		assertThat(updatedEntity.getInstanceType()).isEqualTo(newInstanceType);
		assertThat(updatedEntity.getBaseUrl()).isEqualTo(newBaseUrl);
		assertThat(updatedEntity.getUsername()).isEqualTo(newUsername);
		assertThat(updatedEntity.getFamilyIds()).isEqualTo(newFamilyIds);
		assertThat(updatedEntity.getConnectTimeout()).isEqualTo(newConnectTimeout);
		assertThat(updatedEntity.getReadTimeout()).isEqualTo(newReadTimeout);
		assertThat(updatedEntity.getPassword()).isEqualTo(newPassword);
	}
}
