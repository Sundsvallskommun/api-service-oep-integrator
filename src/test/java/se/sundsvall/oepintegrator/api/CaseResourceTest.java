package se.sundsvall.oepintegrator.api;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeResponse;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.service.CaseService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class CaseResourceTest {

	private static final String PATH = "/{municipalityId}/{instanceType}/cases";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private CaseService caseServiceMock;

	@Test
	void setStatusWithFlowInstanceId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var statusName = "statusName";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = CaseStatusChangeRequest.create()
			.withName(statusName)
			.withPrincipal(principal);
		final var response = new CaseStatusChangeResponse().withEventId(1);

		// Mock
		when(caseServiceMock.setStatusByFlowinstanceId(municipalityId, instanceType, request, flowInstanceId)).thenReturn(response);

		// Act
		webTestClient.put()
			.uri(builder -> builder.path(PATH + "/{flowInstanceId}/status").build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CaseStatusChangeResponse.class)
			.returnResult()
			.getResponseBody();

		// Verify
		verify(caseServiceMock).setStatusByFlowinstanceId(municipalityId, instanceType, request, flowInstanceId);
		verifyNoMoreInteractions(caseServiceMock);
	}

	@Test
	void setStatusWithFlowInstanceIdNoPrincipal() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var statusName = "statusName";
		final var request = CaseStatusChangeRequest.create()
			.withName(statusName);

		// Mock
		when(caseServiceMock.setStatusByFlowinstanceId(municipalityId, instanceType, request, flowInstanceId)).thenReturn(new CaseStatusChangeResponse().withEventId(1));

		// Act
		webTestClient.put()
			.uri(builder -> builder.path(PATH + "/{flowInstanceId}/status").build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CaseStatusChangeResponse.class)
			.returnResult()
			.getResponseBody();

		// Verify
		verify(caseServiceMock).setStatusByFlowinstanceId(municipalityId, instanceType, request, flowInstanceId);
		verifyNoMoreInteractions(caseServiceMock);
	}

	@Test
	void setStatusWithExternalId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var externalId = "externalId";
		final var system = "system";
		final var statusName = "statusName";
		final var userId = "userId";
		final var name = "name";

		final var principal = Principal.create().withUserId(userId).withName(name);
		final var request = CaseStatusChangeRequest.create()
			.withName(statusName)
			.withPrincipal(principal);

		// Mock
		when(caseServiceMock.setStatusByExternalId(municipalityId, instanceType, request, system, externalId)).thenReturn(new CaseStatusChangeResponse().withEventId(1));

		// Act
		webTestClient.put()
			.uri(builder -> builder.path(PATH + "/systems/{system}/{externalId}/status")
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "system", system, "externalId", externalId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CaseStatusChangeResponse.class)
			.returnResult()
			.getResponseBody();

		// Verify
		verify(caseServiceMock).setStatusByExternalId(municipalityId, instanceType, request, system, externalId);
		verifyNoMoreInteractions(caseServiceMock);
	}

	@Test
	void getCasesByFamilyId() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var status = "status";
		final var fromDate = now();
		final var toDate = now();

		when(caseServiceMock.getCaseEnvelopeListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate)).thenReturn(List.of(CaseEnvelope.create()));

		// Act
		final var result = webTestClient.get()
			.uri(builder -> builder.path(PATH + "/families/{familyId}")
				.queryParam("fromDate", List.of(fromDate))
				.queryParam("toDate", List.of(toDate))
				.queryParam("status", List.of(status))
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "familyId", familyId)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(CaseEnvelope.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst()).isNotNull();

		verify(caseServiceMock).getCaseEnvelopeListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate);
	}

	@Test
	void getCasesByCitizenIdentifier() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var partyId = "partyId";
		final var status = "status";
		final var fromDate = now();
		final var toDate = now();

		when(caseServiceMock.getCaseEnvelopeListByCitizenIdentifier(municipalityId, instanceType, partyId, status, fromDate, toDate)).thenReturn(List.of(CaseEnvelope.create()));

		// Act
		final var result = webTestClient.get()
			.uri(builder -> builder.path(PATH + "/parties/{partyId}")
				.queryParam("fromDate", List.of(fromDate))
				.queryParam("toDate", List.of(toDate))
				.queryParam("status", List.of(status))
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "partyId", partyId)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(CaseEnvelope.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst()).isNotNull();

		verify(caseServiceMock).getCaseEnvelopeListByCitizenIdentifier(municipalityId, instanceType, partyId, status, fromDate, toDate);
	}

	@Test
	void confirmDelivery() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var request = new ConfirmDeliveryRequest().withCaseId("caseId").withDelivered(true).withLogMessage("logMessage").withSystem("system");

		// Act
		webTestClient.post()
			.uri(builder -> builder.path(PATH + "/{flowInstanceId}/delivery").build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().doesNotExist("Content-Type")
			.expectBody()
			.returnResult();

		verify(caseServiceMock).confirmDelivery(municipalityId, instanceType, flowInstanceId, request);
	}

	@Test
	void getCasePdfByFlowInstanceId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";

		// Act
		webTestClient.get()
			.uri(builder -> builder.path(PATH + "/{flowInstanceId}/pdf").build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk();

		verify(caseServiceMock).getCasePdfByFlowInstanceId(eq(municipalityId), eq(instanceType), eq(flowInstanceId), any(HttpServletResponse.class));
		verifyNoMoreInteractions(caseServiceMock);
	}

	@Test
	void getCaseStatusByFlowInstanceId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";

		// Act
		webTestClient.get()
			.uri(builder -> builder.path(PATH + "/{flowInstanceId}/status").build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId)))
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk();

		verify(caseServiceMock).getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
		verifyNoMoreInteractions(caseServiceMock);
	}

	@Test
	void getCaseAttachment() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var queryId = "456";
		final var fileId = "789";

		// Act
		webTestClient.get()
			.uri(builder -> builder.path(PATH + "/{flowInstanceId}/queries/{queryId}/files/{fileId}")
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "flowInstanceId", flowInstanceId, "queryId", queryId, "fileId", fileId)))
			.exchange()
			.expectStatus().isOk();

		verify(caseServiceMock).getCaseAttachment(eq(municipalityId), eq(instanceType), eq(flowInstanceId), eq(queryId), eq(fileId), any(HttpServletResponse.class));
		verifyNoMoreInteractions(caseServiceMock);
	}
}
