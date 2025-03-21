package se.sundsvall.oepintegrator.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.zalando.problem.Status.BAD_REQUEST;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class CaseResourceFailureTest {

	private static final String PATH_EXTERNAL_ID = "/{municipalityId}/{instanceType}/cases/systems/{system}/external-id/{externalId}/status";
	private static final String PATH_FLOW_INSTANCE_ID = "/{municipalityId}/{instanceType}/cases/flow-instances/{flowInstanceId}/status";

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void setStatusWithFlowInstanceIdNoStatusOrStatusId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = SetStatusRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
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
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("setStatus.setStatusRequest", "must have a status or a statusId"));
		// TODO verify no service call
	}

	@Test
	void setStatusWithFlowInstanceIdNoUserId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var name = "name";

		final var principal = Principal.create().withName(name);
		final var request = SetStatusRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
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
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("principal.userId", "must not be blank"));
		// TODO verify no service call
	}

	@Test
	void setStatusWithFlowInstanceIdNoRequest() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo("""
			Required request body is missing: org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.oepintegrator.api.CaseResource\
			.setStatus(java.lang.String,se.sundsvall.oepintegrator.utility.enums.InstanceType,java.lang.String,se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest)""");
		// TODO verify no service call
	}

	@Test
	void setStatusWithExternalIdNoStatusOrStatusId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var externalId = "externalId";
		final var system = "system";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = SetStatusRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_EXTERNAL_ID)
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "system", system, "externalId", externalId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
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
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("setStatus.setStatusRequest", "must have a status or a statusId"));
		// TODO verify no service call
	}

	@Test
	void setStatusWithExternalIdNoUserId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var externalId = "externalId";
		final var system = "system";
		final var name = "name";

		final var principal = Principal.create().withName(name);
		final var request = SetStatusRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_EXTERNAL_ID)
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "system", system, "externalId", externalId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
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
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("principal.userId", "must not be blank"));
		// TODO verify no service call
	}

	@Test
	void setStatusWithExternalIdNoRequest() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var externalId = "externalId";
		final var system = "system";

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_EXTERNAL_ID)
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "system", system, "externalId", externalId)))
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo("""
			Required request body is missing: org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.oepintegrator.api.CaseResource\
			.setStatus(java.lang.String,se.sundsvall.oepintegrator.utility.enums.InstanceType,java.lang.String,java.lang.String,se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest)""");
		// TODO verify no service call
	}
}
