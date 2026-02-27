package se.sundsvall.oepintegrator.api;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.problem.violations.Violation;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.service.WebmessageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.INTERNAL;

@AutoConfigureWebTestClient
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class WebmessageResourceFailureTest {

	private static final String PATH = "/{municipalityId}/{instanceType}/webmessages";

	@MockitoSpyBean
	private WebmessageService webmessageService;

	@Autowired
	private WebTestClient webTestClient;

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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("sender", "all attributes are empty. One of the attributes must be set"));

		verifyNoInteractions(webmessageService);
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("sender", "only one of the attributes can be set at a time"));

		verifyNoInteractions(webmessageService);
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("sender.partyId", "not a valid UUID"));

		verifyNoInteractions(webmessageService);
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("message", "must not be blank"));

		verifyNoInteractions(webmessageService);
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("message", "must not be blank"));

		verifyNoInteractions(webmessageService);
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("createWebmessage.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(webmessageService);
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
		assertThat(response.getDetail()).contains("Failed to convert 'instanceType' with value: 'invalidType'");

		verifyNoInteractions(webmessageService);
	}

	@Test
	void createWebmessageWithInvalidExternalReference() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("invalid").withValue("abc")));
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

		verify(webmessageService).createWebmessage(eq("2281"), eq(EXTERNAL), eq(request), any());
	}

	@Test
	void createWebmessageWithInvalidFlowInstanceIdValue() {
		final WebmessageRequest request = WebmessageRequest.create()
			.withSender(Sender.create().withUserId("joe01doe"))
			.withMessage("This is a message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("abc")));
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("externalReferences", "element with key 'flowInstanceId' must have value of numeric type"));

		verifyNoInteractions(webmessageService);
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("externalReferences", "can not be empty or contain elements with empty keys or values"));

		verifyNoInteractions(webmessageService);
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
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getWebmessagesByFamilyId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(webmessageService);
	}

	@Test
	void getWebmessagesByFlowInstanceIdWithInvalidMunicipalityId() {

		final var response = webTestClient.get()
			.uri(PATH + ("/cases/{flowInstanceId}"), "invalidId", INTERNAL, 123)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getWebmessagesByFlowInstanceId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(webmessageService);
	}

	@Test
	void getAttachmentByIdWithInvalidMunicipalityId() {
		final var response = webTestClient.get()
			.uri(PATH + ("/attachments/{attachmentId}"), "invalidId", INTERNAL, 123)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactly(tuple("getAttachmentById.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(webmessageService);
	}
}
