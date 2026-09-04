package se.sundsvall.oepintegrator.api;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.problem.violations.Violation;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.instance.Instance;
import se.sundsvall.oepintegrator.service.InstanceService;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.util.enums.IntegrationType.REST;

@AutoConfigureWebTestClient
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class InstanceResourceFailureTest {

	private static final String PATH = "/{municipalityId}/instances";
	private static final String PATH_WITH_INSTANCE_ID = "/{municipalityId}/instances/{instanceId}";
	private static final String INVALID = "invalid";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private InstanceService instanceServiceMock;

	private static Instance createInstance() {
		return Instance.create()
			.withIntegrationType(REST)
			.withInstanceType(EXTERNAL)
			.withBaseUrl("https://example.com")
			.withUsername("user123")
			.withPassword("pass123")
			.withConnectTimeout(5)
			.withReadTimeout(60);
	}

	@Test
	void getInstancesWithInvalidMunicipalityId() {
		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH).build(Map.of("municipalityId", INVALID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getInstances.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(instanceServiceMock);
	}

	@Test
	void getInstanceWithInvalidMunicipalityId() {
		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_WITH_INSTANCE_ID).build(Map.of("municipalityId", INVALID, "instanceId", randomUUID().toString())))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getInstance.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(instanceServiceMock);
	}

	@Test
	void getInstanceWithInvalidInstanceId() {
		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_WITH_INSTANCE_ID).build(Map.of("municipalityId", "2281", "instanceId", INVALID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getInstance.instanceId", "not a valid UUID"));

		verifyNoInteractions(instanceServiceMock);
	}

	@Test
	void createInstanceWithInvalidMunicipalityId() {
		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH).build(Map.of("municipalityId", INVALID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(createInstance())
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("createInstance.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(instanceServiceMock);
	}

	@Test
	void updateInstanceWithInvalidInstanceId() {
		// Act
		final var response = webTestClient.patch()
			.uri(builder -> builder.path(PATH_WITH_INSTANCE_ID).build(Map.of("municipalityId", "2281", "instanceId", INVALID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(createInstance())
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("updateInstance.instanceId", "not a valid UUID"));

		verifyNoInteractions(instanceServiceMock);
	}

	@Test
	void deleteInstanceWithInvalidInstanceId() {
		// Act
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH_WITH_INSTANCE_ID).build(Map.of("municipalityId", "2281", "instanceId", INVALID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("deleteInstance.instanceId", "not a valid UUID"));

		verifyNoInteractions(instanceServiceMock);
	}
}
