package se.sundsvall.oepintegrator.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.zalando.problem.Status.BAD_REQUEST;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.WebMessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class WebMessageResourceFailureTest {

	private static final String PATH = "/{municipalityId}/{instanceType}/webmessages";

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void testCreateWebMessageWithNullSender() {
		final WebMessageRequest request = WebMessageRequest.create()
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, MediaType.APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", InstanceType.EXTERNAL)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("sender", "must not be null"));
	}

	@Test
	void testCreateWebMessageWithBlankMessage() {
		final WebMessageRequest request = WebMessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, MediaType.APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", InstanceType.INTERNAL)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("message", "must not be blank"));
	}

	@Test
	void testCreateWebMessageWithNullMessage() {
		final WebMessageRequest request = WebMessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, MediaType.APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", InstanceType.EXTERNAL)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("message", "must not be blank"));
	}

	@Test
	void testCreateWebMessageWithInvalidMunicipalityId() {
		final WebMessageRequest request = WebMessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, MediaType.APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "invalidId", InstanceType.INTERNAL)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("createWebMessage.municipalityId", "not a valid municipality ID"));
	}

	@Test
	void testCreateWebMessageWithInvalidInstanceType() {
		final WebMessageRequest request = WebMessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));
		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, MediaType.APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", "invalidType")
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(Problem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).contains("Method parameter 'instanceType': Failed to convert value of type 'java.lang.String' to required type 'se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType'");
	}

	@Test
	void testCreateWebMessageWithInvalidExternalReference() {
		final WebMessageRequest request = WebMessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("invalid").withValue("123")));
		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, MediaType.APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", InstanceType.EXTERNAL)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(Problem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).contains("Flow instance id is required");
	}

	@Test
	void testCreateWebMessageWithNullExternalReference() {
		final WebMessageRequest request = WebMessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message");
		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, MediaType.APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", InstanceType.EXTERNAL)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("externalReferences", "can not be empty or contain elements with empty keys or values"));
	}

}
