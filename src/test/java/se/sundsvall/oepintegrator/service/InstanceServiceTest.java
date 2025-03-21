package se.sundsvall.oepintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.INTERNAL;
import static se.sundsvall.oepintegrator.utility.enums.IntegrationType.REST;
import static se.sundsvall.oepintegrator.utility.enums.IntegrationType.SOAP;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.Instance;
import se.sundsvall.oepintegrator.integration.db.InstanceRepository;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import se.sundsvall.oepintegrator.utility.EncryptionUtility;

@ExtendWith(MockitoExtension.class)
class InstanceServiceTest {

	@Mock
	private InstanceRepository instanceRepositoryMock;

	@Mock
	private EncryptionUtility encryptionUtilityMock;

	@Mock
	private OpeneClientFactory clientFactoryMock;

	@InjectMocks
	private InstanceService instanceService;

	@Captor
	private ArgumentCaptor<InstanceEntity> instanceEntityCaptor;

	@Test
	void getInstances() {
		// Arrange
		final var municipalityId = "2281";
		final var instance = InstanceEntity.create();

		when(instanceRepositoryMock.findByMunicipalityId(municipalityId)).thenReturn(List.of(instance));

		// Act
		final var result = instanceService.getInstances(municipalityId);

		// Assert
		assertThat(result).isNotNull().hasSize(1);

		verify(instanceRepositoryMock).findByMunicipalityId(municipalityId);
		verifyNoMoreInteractions(instanceRepositoryMock);
		verifyNoInteractions(encryptionUtilityMock, clientFactoryMock);
	}

	@Test
	void getInstance() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";
		final var instance = InstanceEntity.create();

		when(instanceRepositoryMock.findByMunicipalityIdAndId(municipalityId, instanceId)).thenReturn(Optional.of(instance));

		// Act
		final var result = instanceService.getInstance(municipalityId, instanceId);

		// Assert
		assertThat(result).isNotNull();

		verify(instanceRepositoryMock).findByMunicipalityIdAndId(municipalityId, instanceId);
		verifyNoMoreInteractions(instanceRepositoryMock);
		verifyNoInteractions(encryptionUtilityMock, clientFactoryMock);
	}

	@Test
	void getInstanceThrowsException() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";

		when(instanceRepositoryMock.findByMunicipalityIdAndId(municipalityId, instanceId)).thenReturn(Optional.empty());

		// Act
		assertThatThrownBy(() -> instanceService.getInstance(municipalityId, instanceId))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("title", "Not Found")
			.hasMessage(String.format("Not Found: An instance with id '%s' could not be found in municipality with id '%s'", instanceId, municipalityId));

		verify(instanceRepositoryMock).findByMunicipalityIdAndId(municipalityId, instanceId);
		verifyNoMoreInteractions(instanceRepositoryMock);
		verifyNoInteractions(encryptionUtilityMock, clientFactoryMock);
	}

	@Test
	void createInstance() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";
		final var instance = Instance.create().withPassword("someNewPassword");
		final var encryptedPassword = "encryptedPassword";
		final var instanceEntity = InstanceEntity.create().withId(instanceId).withMunicipalityId(municipalityId).withPassword(encryptedPassword);

		when(encryptionUtilityMock.encrypt(instance.getPassword().getBytes())).thenReturn(encryptedPassword);
		when(instanceRepositoryMock.save(any(InstanceEntity.class))).thenReturn(instanceEntity);

		// Act
		final var result = instanceService.createInstance(municipalityId, instance);

		// Assert
		assertThat(result).isEqualTo(instanceId);
		verify(instanceRepositoryMock).save(instanceEntityCaptor.capture());

		final var capturedInstanceEntity = instanceEntityCaptor.getValue();
		assertThat(capturedInstanceEntity).isNotNull();
		assertThat(capturedInstanceEntity.getPassword()).isEqualTo(encryptedPassword);
		assertThat(capturedInstanceEntity.getMunicipalityId()).isEqualTo(municipalityId);

		verify(encryptionUtilityMock).encrypt(instance.getPassword().getBytes());
		verify(clientFactoryMock).createClient(instanceEntity);
		verifyNoMoreInteractions(instanceRepositoryMock, encryptionUtilityMock, clientFactoryMock);
	}

	@Test
	void updateInstance() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";
		final var instanceEntity = InstanceEntity.create().withInstanceType(INTERNAL).withIntegrationType(REST);
		final var instance = Instance.create().withPassword("someNewPassword").withInstanceType(INTERNAL).withIntegrationType(REST);

		when(instanceRepositoryMock.findByMunicipalityIdAndId(municipalityId, instanceId)).thenReturn(Optional.of(instanceEntity));

		// Act
		instanceService.updateInstance(municipalityId, instanceId, instance);

		// Assert
		verify(instanceRepositoryMock).findByMunicipalityIdAndId(municipalityId, instanceId);
		verify(instanceRepositoryMock).save(instanceEntity);
		verify(encryptionUtilityMock).encrypt(instance.getPassword().getBytes());
		verify(clientFactoryMock).createClient(instanceEntity);
		verify(clientFactoryMock).removeClient(municipalityId, INTERNAL, REST);
		verifyNoMoreInteractions(instanceRepositoryMock, clientFactoryMock);
	}

	@Test
	void updateInstanceThrowsException() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";
		final var instance = Instance.create().withPassword("someNewPassword").withInstanceType(INTERNAL);

		when(instanceRepositoryMock.findByMunicipalityIdAndId(municipalityId, instanceId)).thenReturn(Optional.empty());

		// Act
		assertThatThrownBy(() -> instanceService.updateInstance(municipalityId, instanceId, instance))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("title", "Not Found")
			.hasMessage(String.format("Not Found: An instance with id '%s' could not be found in municipality with id '%s'", instanceId, municipalityId));

		verify(instanceRepositoryMock).findByMunicipalityIdAndId(municipalityId, instanceId);
		verifyNoMoreInteractions(instanceRepositoryMock);
		verifyNoInteractions(encryptionUtilityMock, clientFactoryMock);

	}

	@Test
	void deleteInstance() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";
		final var instanceEntity = InstanceEntity.create().withInstanceType(INTERNAL).withIntegrationType(SOAP);

		when(instanceRepositoryMock.findByMunicipalityIdAndId(municipalityId, instanceId)).thenReturn(Optional.of(instanceEntity));

		// Act
		instanceService.deleteInstance(municipalityId, instanceId);

		// Assert
		verify(instanceRepositoryMock).findByMunicipalityIdAndId(municipalityId, instanceId);
		verify(instanceRepositoryMock).deleteById(instanceId);
		verify(clientFactoryMock).removeClient(municipalityId, INTERNAL, SOAP);
		verifyNoMoreInteractions(instanceRepositoryMock, clientFactoryMock);
		verifyNoInteractions(encryptionUtilityMock);
	}

	@Test
	void deleteInstanceThrowsException() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";

		when(instanceRepositoryMock.findByMunicipalityIdAndId(municipalityId, instanceId)).thenReturn(Optional.empty());

		// Act
		assertThatThrownBy(() -> instanceService.deleteInstance(municipalityId, instanceId))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("title", "Not Found")
			.hasMessage(String.format("Not Found: An instance with id '%s' could not be found in municipality with id '%s'", instanceId, municipalityId));

		verify(instanceRepositoryMock).findByMunicipalityIdAndId(municipalityId, instanceId);
		verifyNoMoreInteractions(instanceRepositoryMock);
		verifyNoInteractions(encryptionUtilityMock, clientFactoryMock);

	}
}
