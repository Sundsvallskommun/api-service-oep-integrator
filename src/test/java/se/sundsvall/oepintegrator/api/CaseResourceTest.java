package se.sundsvall.oepintegrator.api;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType.EXTERNAL;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class CaseResourceTest {

	private static final String PATH = "/{municipalityId}/{instanceType}/cases";

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void setStatusWithFlowInstanceId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var status = "status";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = SetStatusRequest.create()
			.withStatus(status)
			.withPrincipal(principal);

		// Act
		webTestClient.put()
			.uri(builder -> builder.path(PATH + "/flowInstanceId/{flowInstanceId}/status").build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL)
			.expectBody().isEmpty();

		// Assert
		// TODO verify service call
	}

	void setStatusWithFlowInstanceIdNoPrincipal() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var status = "status";
		final var request = SetStatusRequest.create()
			.withStatus(status);

		// Act
		webTestClient.put()
			.uri(builder -> builder.path(PATH + "/flowInstanceId/{flowInstanceId}/status").build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL)
			.expectBody().isEmpty();

		// Assert
		// TODO verify service call
	}

	@Test
	void setStatusWithExternalId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var externalId = "externalId";
		final var system = "system";
		final var status = "status";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = SetStatusRequest.create()
			.withStatus(status)
			.withPrincipal(principal);

		// Act
		webTestClient.put()
			.uri(builder -> builder.path(PATH + "/system/{system}/externalId/{externalId}/status")
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "system", system, "externalId", externalId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL)
			.expectBody().isEmpty();

		// Assert
		// TODO verify service call
	}
}
