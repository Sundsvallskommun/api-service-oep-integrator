package se.sundsvall.oepintegrator.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.util.enums.IntegrationType.REST;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.Instance;
import se.sundsvall.oepintegrator.service.InstanceService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class InstanceResourceTest {

	private static final String PATH = "/{municipalityId}/instance";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private InstanceService instanceService;

	@Test
	void getInstances() {
		// Arrange
		final var municipalityId = "2281";
		final var integrationType = REST;
		final var instanceType = EXTERNAL;
		final var baseUrl = "https://example.com";
		final var username = "user123";
		final var password = "pass123";
		final var familyIds = List.of("family1", "family2");
		final var connectTimeout = 5;
		final var readTimeout = 60;

		final var instance = Instance.create()
			.withIntegrationType(integrationType)
			.withInstanceType(instanceType)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withPassword(password)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		when(instanceService.getInstances(municipalityId)).thenReturn(List.of(instance));

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH).build(Map.of("municipalityId", municipalityId)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(Instance.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull().hasSize(1);
		assertThat(response.getFirst()).isEqualTo(instance);
		verify(instanceService).getInstances(municipalityId);
		verifyNoMoreInteractions(instanceService);
	}

	@Test
	void getInstance() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceId = UUID.randomUUID().toString();
		final var integrationType = REST;
		final var instanceType = EXTERNAL;
		final var baseUrl = "https://example.com";
		final var username = "user123";
		final var password = "pass123";
		final var familyIds = List.of("family1", "family2");
		final var connectTimeout = 5;
		final var readTimeout = 60;

		final var instance = Instance.create()
			.withIntegrationType(integrationType)
			.withInstanceType(instanceType)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withPassword(password)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		when(instanceService.getInstance(municipalityId, instanceId)).thenReturn(instance);

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH + "/{instanceId}").build(Map.of("municipalityId", municipalityId, "instanceId", instanceId)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(Instance.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull().isEqualTo(instance);
		verify(instanceService).getInstance(municipalityId, instanceId);
		verifyNoMoreInteractions(instanceService);
	}

	@Test
	void createInstance() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceId = "1234";
		final var integrationType = REST;
		final var instanceType = EXTERNAL;
		final var baseUrl = "https://example.com";
		final var username = "user123";
		final var password = "pass123";
		final var familyIds = List.of("family1", "family2");
		final var connectTimeout = 5;
		final var readTimeout = 60;

		final var instance = Instance.create()
			.withIntegrationType(integrationType)
			.withInstanceType(instanceType)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withPassword(password)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		when(instanceService.createInstance(municipalityId, instance)).thenReturn(instanceId);

		// Act
		webTestClient.post()
			.uri(builder -> builder.path(PATH).build(Map.of("municipalityId", municipalityId)))
			.bodyValue(instance)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL)
			.expectHeader().location("/" + municipalityId + "/instance/" + instanceId)
			.expectBody().isEmpty();

		// Assert
		verify(instanceService).createInstance(municipalityId, instance);
		verifyNoMoreInteractions(instanceService);
	}

	@Test
	void updateInstance() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceId = UUID.randomUUID().toString();
		final var integrationType = REST;
		final var instanceType = EXTERNAL;
		final var baseUrl = "https://example.com";
		final var username = "user123";
		final var password = "pass123";
		final var familyIds = List.of("family1", "family2");
		final var connectTimeout = 5;
		final var readTimeout = 60;

		final var instance = Instance.create()
			.withIntegrationType(integrationType)
			.withInstanceType(instanceType)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withPassword(password)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		// Act
		webTestClient.patch()
			.uri(builder -> builder.path(PATH + "/{instanceId}").build(Map.of("municipalityId", municipalityId, "instanceId", instanceId)))
			.bodyValue(instance)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL)
			.expectBody().isEmpty();

		// Assert
		verify(instanceService).updateInstance(municipalityId, instanceId, instance);
		verifyNoMoreInteractions(instanceService);
	}

	@Test
	void deleteInstance() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceId = UUID.randomUUID().toString();

		// Act
		webTestClient.delete()
			.uri(builder -> builder.path(PATH + "/{instanceId}").build(Map.of("municipalityId", municipalityId, "instanceId", instanceId)))
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL)
			.expectBody().isEmpty();

		// Assert
		verify(instanceService).deleteInstance(municipalityId, instanceId);
		verifyNoMoreInteractions(instanceService);
	}
}
