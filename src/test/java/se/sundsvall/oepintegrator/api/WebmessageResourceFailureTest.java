package se.sundsvall.oepintegrator.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.zalando.problem.Status.BAD_REQUEST;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.INTERNAL;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class WebmessageResourceFailureTest {

	private static final String PATH = "/{municipalityId}/{instanceType}/webmessages";

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void createWebmessageWithNullSender() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", EXTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
	void createWebmessageWithAllSenderAttributesNull() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withMessage("This is a message")
			.withSender(Sender.create())
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", EXTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
			.containsExactly(tuple("sender", "all attributes are empty. One of the attributes must be set"));
	}

	@Test
	void createWebmessageWithMoreThanOneSenderAttributeSet() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withMessage("This is a message")
			.withSender(Sender.create()
				.withAdministratorId("administratorId")
				.withUserId("userId"))
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", EXTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
			.containsExactly(tuple("sender", "only one of the attributes can be set at a time"));
	}

	@Test
	void createWebmessageWithInvalidSenderPartyId() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withMessage("This is a message")
			.withSender(Sender.create()
				.withPartyId("invalid"))
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", EXTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
			.containsExactly(tuple("sender.partyId", "not a valid UUID"));
	}

	@Test
	void createWebmessageWithBlankMessage() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", INTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
	void createWebmessageWithNullMessage() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", EXTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
	void createWebmessageWithInvalidMunicipalityId() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "invalidId", INTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
			.containsExactly(tuple("createWebmessage.municipalityId", "not a valid municipality ID"));
	}

	@Test
	void createWebmessageWithInvalidInstanceType() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123")));
		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", "invalidType")
			.contentType(MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(Problem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).contains("Method parameter 'instanceType': Failed to convert value of type 'java.lang.String' to required type 'se.sundsvall.oepintegrator.utility.enums.InstanceType'");
	}

	@Test
	void createWebmessageWithInvalidExternalReference() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("invalid").withValue("123")));
		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", EXTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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
	void createWebmessageWithNullExternalReference() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message");
		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var response = webTestClient.post()
			.uri(PATH, "2281", EXTERNAL)
			.contentType(MULTIPART_FORM_DATA)
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

	@Test
	void getWebmessagesByFamilyIdWithInvalidMunicipalityId() {

		final var response = webTestClient.get()
			.uri(PATH + ("/families/{familyId}}"), "invalidId", INTERNAL, 123)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getWebmessagesByFamilyId.municipalityId", "not a valid municipality ID"));
	}

	@Test
	void getWebmessagesByFlowInstanceIdWithInvalidMunicipalityId() {

		final var response = webTestClient.get()
			.uri(PATH + ("/flow-instances/{flowInstanceId}"), "invalidId", INTERNAL, 123)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getWebmessagesByFlowInstanceId.municipalityId", "not a valid municipality ID"));
	}

	@Test
	void getAttachmentByIdWithInvalidMunicipalityId() {
		final var response = webTestClient.get()
			.uri(PATH + ("/flow-instances/{flowInstanceId}/attachments/{attachmentId}"), "invalidId", INTERNAL, 123, 123)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getAttachmentById.municipalityId", "not a valid municipality ID"));
	}
}
