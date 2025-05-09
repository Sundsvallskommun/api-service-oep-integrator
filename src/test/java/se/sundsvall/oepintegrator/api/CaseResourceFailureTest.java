package se.sundsvall.oepintegrator.api;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.INTERNAL;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.service.CaseService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class CaseResourceFailureTest {

	private static final String PATH_SET_STATUS_BY_EXTERNAL_ID = "/{municipalityId}/{instanceType}/cases/systems/{system}/{externalId}/status";
	private static final String PATH_SET_STATUS_BY_FLOW_INSTANCE_ID = "/{municipalityId}/{instanceType}/cases/{flowInstanceId}/status";
	private static final String PATH_GET_CASES_BY_FAMILY_ID = "/{municipalityId}/{instanceType}/cases/families/{familyId}";
	private static final String PATH_GET_CASES_BY_PARTY_ID = "/{municipalityId}/{instanceType}/cases/parties/{partyId}";
	private static final String PATH_CONFIRM_DELIVERY = "/{municipalityId}/{instanceType}/cases/{flowInstanceId}/delivery";
	private static final String PATH_GET_CASE_PDF_BY_FLOW_INSTANCE_ID = "/{municipalityId}/{instanceType}/cases/{flowInstanceId}/pdf";
	private static final String PATH_GET_CASE_STATUS_BY_FLOW_INSTANCE_ID = "/{municipalityId}/{instanceType}/cases/{flowInstanceId}/status";
	private static final String PATH_GET_CASE_ATTACHMENT = "/{municipalityId}/{instanceType}/cases/{flowInstanceId}/queries/{queryId}/files/{fileId}";
	private static final String PATH_GET_CASE = "/{municipalityId}/{instanceType}/cases/{flowInstanceId}";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private CaseService caseServiceMock;

	@Test
	void setCaseStatusWithFlowInstanceIdNoStatusOrStatusId() {
		// Arrange
		final var municipalityId = "2281";
		final var flowInstanceId = "123";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = CaseStatusChangeRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_SET_STATUS_BY_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", EXTERNAL, "flowInstanceId", flowInstanceId)))
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

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void setCaseStatusWithFlowInstanceIdNoUserId() {
		// Arrange
		final var municipalityId = "2281";
		final var flowInstanceId = "123";
		final var name = "name";

		final var principal = Principal.create().withName(name);
		final var request = CaseStatusChangeRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_SET_STATUS_BY_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", EXTERNAL, "flowInstanceId", flowInstanceId)))
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

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void setCaseStatusWithFlowInstanceIdNoRequest() {
		// Arrange
		final var municipalityId = "2281";
		final var flowInstanceId = "123";

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_SET_STATUS_BY_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", EXTERNAL, "flowInstanceId", flowInstanceId)))
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
			Required request body is missing: org.springframework.http.ResponseEntity<se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeResponse> \
			se.sundsvall.oepintegrator.api.CaseResource.setStatus(java.lang.String,se.sundsvall.oepintegrator.util.enums.InstanceType,java.lang.String,se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest)""");

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void setCaseStatusWithExternalIdNoStatusOrStatusId() {
		// Arrange
		final var municipalityId = "2281";
		final var externalId = "externalId";
		final var system = "system";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = CaseStatusChangeRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_SET_STATUS_BY_EXTERNAL_ID)
				.build(Map.of("municipalityId", municipalityId, "instanceType", EXTERNAL, "system", system, "externalId", externalId)))
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

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void setCaseStatusWithExternalIdNoUserId() {
		// Arrange
		final var municipalityId = "2281";
		final var externalId = "externalId";
		final var system = "system";
		final var name = "name";

		final var principal = Principal.create().withName(name);
		final var request = CaseStatusChangeRequest.create()
			.withPrincipal(principal);

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_SET_STATUS_BY_EXTERNAL_ID)
				.build(Map.of("municipalityId", municipalityId, "instanceType", EXTERNAL, "system", system, "externalId", externalId)))
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

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void setCaseStatusWithExternalIdNoRequest() {
		// Arrange
		final var municipalityId = "2281";
		final var externalId = "externalId";
		final var system = "system";

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH_SET_STATUS_BY_EXTERNAL_ID)
				.build(Map.of("municipalityId", municipalityId, "instanceType", EXTERNAL, "system", system, "externalId", externalId)))
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
			Required request body is missing: org.springframework.http.ResponseEntity<se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeResponse> \
			se.sundsvall.oepintegrator.api.CaseResource.setStatus(java.lang.String,se.sundsvall.oepintegrator.util.enums.InstanceType,java.lang.String,java.lang.String,\
			se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest)""");

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCasesByFamilyIdWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.get()
			.uri(PATH_GET_CASES_BY_FAMILY_ID, "invalidId", INTERNAL, 123)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getCasesByFamilyId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCasesByPartyIdWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.get()
			.uri(PATH_GET_CASES_BY_PARTY_ID, "invalidId", INTERNAL, randomUUID().toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getCasesByPartyId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCasesByPartyIdWithInvalidPartyId() {
		// Arrange
		final var municipalityId = "2281";

		// Act
		final var response = webTestClient.get()
			.uri(PATH_GET_CASES_BY_PARTY_ID, municipalityId, INTERNAL, 123)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getCasesByPartyId.partyId", "not a valid UUID"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void confirmDeliveryWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH_CONFIRM_DELIVERY).build(Map.of("municipalityId", "invalidId", "instanceType", INTERNAL, "flowInstanceId", 123)))
			.contentType(APPLICATION_JSON)
			.bodyValue(new ConfirmDeliveryRequest().withCaseId("someCaseId").withDelivered(true).withLogMessage("logMessage").withSystem("system"))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("confirmDelivery.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void confirmDeliveryWithInvalidRequest() {

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH_CONFIRM_DELIVERY).build(Map.of("municipalityId", "2281", "instanceType", INTERNAL, "flowInstanceId", 123)))
			.contentType(APPLICATION_JSON)
			.bodyValue(new ConfirmDeliveryRequest())
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("caseId", "must not be blank"),
				tuple("system", "must not be blank"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCasePdfByFlowInstanceIdWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_GET_CASE_PDF_BY_FLOW_INSTANCE_ID).build(Map.of("municipalityId", "invalidId", "instanceType", INTERNAL, "flowInstanceId", 123)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getCasePdfByFlowInstanceId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCasePdfByFlowInstanceIdNotFound() {
		// Arrange
		final var municipalityId = "2281";
		final var flowInstanceId = "123";

		doThrow(Problem.valueOf(NOT_FOUND, "PDF for flow instance ID '123' not found"))
			.when(caseServiceMock).getCasePdfByFlowInstanceId(eq(municipalityId), eq(INTERNAL), eq(flowInstanceId), any());

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_GET_CASE_PDF_BY_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", INTERNAL, "flowInstanceId", flowInstanceId)))
			.exchange()
			.expectStatus().isNotFound()
			.expectBody(Problem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Not Found");
		assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(response.getDetail()).isEqualTo("PDF for flow instance ID '123' not found");

		verify(caseServiceMock).getCasePdfByFlowInstanceId(eq(municipalityId), eq(INTERNAL), eq(flowInstanceId), any());
		verifyNoMoreInteractions(caseServiceMock);
	}

	@Test
	void getCaseStatusByFlowInstanceIdNotFound() {
		// Arrange
		final var municipalityId = "2281";
		final var flowInstanceId = "123";

		doThrow(Problem.valueOf(NOT_FOUND, "Status for flow instance ID '123' not found"))
			.when(caseServiceMock).getCaseStatusByFlowInstanceId(municipalityId, INTERNAL, flowInstanceId);

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_GET_CASE_STATUS_BY_FLOW_INSTANCE_ID).build(Map.of("municipalityId", municipalityId, "instanceType", INTERNAL, "flowInstanceId", flowInstanceId)))
			.exchange()
			.expectStatus().isNotFound()
			.expectBody(Problem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Not Found");
		assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(response.getDetail()).isEqualTo("Status for flow instance ID '123' not found");

		verify(caseServiceMock).getCaseStatusByFlowInstanceId(municipalityId, INTERNAL, flowInstanceId);
		verifyNoMoreInteractions(caseServiceMock);
	}

	@Test
	void getCaseStatusByFlowInstanceIdWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_GET_CASE_STATUS_BY_FLOW_INSTANCE_ID).build(Map.of("municipalityId", "invalidId", "instanceType", INTERNAL, "flowInstanceId", 123)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getCaseStatusByFlowInstanceId.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCaseAttachmentWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_GET_CASE_ATTACHMENT).build(Map.of("municipalityId", "invalidId", "instanceType", INTERNAL, "flowInstanceId", 123, "queryId", 456, "fileId", 789)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getCaseAttachment.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCaseWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_GET_CASE).build(Map.of("municipalityId", "invalidId", "instanceType", INTERNAL, "flowInstanceId", 123)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getCaseByFlowInstanceId.municipalityId", "not a valid municipality ID"));
		verifyNoInteractions(caseServiceMock);
	}

	@Test
	void getCaseNotFound() {

		// Arrange
		final var municipalityId = "2281";
		final var flowInstanceId = "123";

		doThrow(Problem.valueOf(NOT_FOUND, "Case with flow instance ID '123' not found"))
			.when(caseServiceMock).getCaseByFlowInstanceId(municipalityId, INTERNAL, flowInstanceId);

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH_GET_CASE).build(Map.of("municipalityId", municipalityId, "instanceType", INTERNAL, "flowInstanceId", flowInstanceId)))
			.exchange()
			.expectStatus().isNotFound()
			.expectBody(Problem.class)
			.returnResult().getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Not Found");
		assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(response.getDetail()).isEqualTo("Case with flow instance ID '123' not found");

		verify(caseServiceMock).getCaseByFlowInstanceId(municipalityId, INTERNAL, flowInstanceId);
		verifyNoMoreInteractions(caseServiceMock);
	}

}
