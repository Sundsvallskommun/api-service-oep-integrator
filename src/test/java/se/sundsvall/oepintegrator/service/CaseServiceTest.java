package se.sundsvall.oepintegrator.service;

import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;

import callback.SetStatusResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.integration.party.PartyClient;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

	@Mock
	private PartyClient partyClientMock;

	@Mock
	private OpeneSoapIntegration openeSoapIntegrationMock;

	@Mock
	private OpeneRestIntegration openeRestIntegrationMock;

	@InjectMocks
	private CaseService caseService;

	@Test
	void confirmDelivery() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var request = new ConfirmDeliveryRequest().withCaseId("caseId").withDelivered(true).withLogMessage("logMessage").withSystem("system");

		// Act
		caseService.confirmDelivery(municipalityId, instanceType, flowInstanceId, request);

		// Assert
		verify(openeSoapIntegrationMock).confirmDelivery(eq(municipalityId), eq(instanceType), any());
		verifyNoMoreInteractions(openeSoapIntegrationMock);
	}

	@Test
	void setStatusByFlowinstanceId() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var eventId = 1234;
		final var request = new CaseStatusChangeRequest().withName("statusName").withPrincipal(new Principal().withName("name").withUserId("userId"));
		final var response = new SetStatusResponse().withEventID(eventId);

		when(openeSoapIntegrationMock.setStatus(eq(municipalityId), eq(instanceType), any())).thenReturn(response);

		// Act
		final var result = caseService.setStatusByFlowinstanceId(municipalityId, instanceType, request, flowInstanceId);

		// Assert
		assertThat(result.getEventId()).isEqualTo(eventId);

		verify(openeSoapIntegrationMock).setStatus(eq(municipalityId), eq(instanceType), any());
		verifyNoMoreInteractions(openeSoapIntegrationMock);
	}

	@Test
	void setStatusByExternalId() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var externalId = "externalId";
		final var system = "system";
		final var eventId = 1234;
		final var request = new CaseStatusChangeRequest().withName("statusName").withPrincipal(new Principal().withName("name").withUserId("userId"));
		final var response = new SetStatusResponse().withEventID(eventId);

		when(openeSoapIntegrationMock.setStatus(eq(municipalityId), eq(instanceType), any())).thenReturn(response);

		// Act
		final var result = caseService.setStatusByExternalId(municipalityId, instanceType, request, externalId, system);

		// Assert
		assertThat(result.getEventId()).isEqualTo(eventId);

		verify(openeSoapIntegrationMock).setStatus(eq(municipalityId), eq(instanceType), any());
		verifyNoMoreInteractions(openeSoapIntegrationMock);
	}

	@Test
	void getCasePdfByFlowInstanceId() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";

		final var mockHttpServletResponse = new MockHttpServletResponse();
		final var headers = Map.of(
			"Content-Type", List.of(APPLICATION_PDF_VALUE),
			"Content-Disposition", List.of("attachment; filename=case.pdf"),
			"Content-Length", List.of("0"),
			"Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[0]));
		final var responseEntity = ok()
			.headers(httpHeaders -> httpHeaders.putAll(headers))
			.body(inputStreamResource);

		when(openeRestIntegrationMock.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId)).thenReturn(responseEntity);

		// Act
		caseService.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId, mockHttpServletResponse);

		// Assert
		assertThat(mockHttpServletResponse.getContentType()).isEqualTo("application/pdf");
		assertThat(mockHttpServletResponse.getHeader("Content-Disposition")).isEqualTo("attachment; filename=case.pdf");
		assertThat(mockHttpServletResponse.getHeader("Content-Length")).isEqualTo("0");
		assertThat(mockHttpServletResponse.getHeader("Last-Modified")).isEqualTo("Wed, 21 Oct 2015 07:28:00 GMT");

		verify(openeRestIntegrationMock).getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
		verifyNoMoreInteractions(openeRestIntegrationMock);
		verifyNoInteractions(openeSoapIntegrationMock);
	}

	@Test
	void getCasePdfByFlowInstanceIdThrowsException() throws IOException {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var mockHttpServletResponse = Mockito.mock(HttpServletResponse.class);
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[10]));
		final var responseEntity = badRequest().body(inputStreamResource);

		when(mockHttpServletResponse.getOutputStream()).thenThrow(new IOException());
		when(openeRestIntegrationMock.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId)).thenReturn(responseEntity);

		// Act
		assertThatThrownBy(() -> caseService.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId, mockHttpServletResponse))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", INTERNAL_SERVER_ERROR)
			.hasMessage("Internal Server Error: Unable to get case pdf");

		verify(openeRestIntegrationMock).getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
		verifyNoMoreInteractions(openeRestIntegrationMock);
		verifyNoInteractions(openeSoapIntegrationMock);
	}

	@Test
	void getCaseEnvelopeListByFamilyId() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var status = "status";
		final var fromDate = LocalDate.of(2023, 1, 1);
		final var toDate = LocalDate.of(2023, 12, 31);
		final var expectedCaseEnvelopeList = List.of(new CaseEnvelope());

		when(openeRestIntegrationMock.getCaseListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate))
			.thenReturn(expectedCaseEnvelopeList);

		// Act
		final var result = caseService.getCaseEnvelopeListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate);

		// Assert
		assertThat(result).isEqualTo(expectedCaseEnvelopeList);

		verify(openeRestIntegrationMock).getCaseListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate);
		verifyNoMoreInteractions(openeRestIntegrationMock);
		verifyNoInteractions(openeSoapIntegrationMock, partyClientMock);
	}

	@Test
	void getCaseEnvelopeListByCitizenIdentifier() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var partyType = PRIVATE;
		final var partyId = "partyId";
		final var legalId = "legalId";
		final var status = "status";
		final var fromDate = LocalDate.of(2023, 1, 1);
		final var toDate = LocalDate.of(2023, 12, 31);
		final var expectedCaseEnvelopeList = List.of(new CaseEnvelope());

		when(partyClientMock.getLegalId(municipalityId, partyType, partyId))
			.thenReturn(Optional.of(legalId));

		when(openeRestIntegrationMock.getCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, status, fromDate, toDate))
			.thenReturn(expectedCaseEnvelopeList);

		// Act
		final var result = caseService.getCaseEnvelopeListByCitizenIdentifier(municipalityId, instanceType, partyId, status, fromDate, toDate);

		// Assert
		assertThat(result).isEqualTo(expectedCaseEnvelopeList);

		verify(openeRestIntegrationMock).getCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, status, fromDate, toDate);
		verify(partyClientMock).getLegalId(municipalityId, partyType, partyId);
		verifyNoMoreInteractions(openeRestIntegrationMock, partyClientMock);
		verifyNoInteractions(openeSoapIntegrationMock);
	}

	@Test
	void getCaseEnvelopeListByCitizenIdentifierPartyNotFound() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var partyType = PRIVATE;
		final var partyId = "partyId";
		final var status = "status";
		final var fromDate = LocalDate.of(2023, 1, 1);
		final var toDate = LocalDate.of(2023, 12, 31);

		when(partyClientMock.getLegalId(municipalityId, partyType, partyId))
			.thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> caseService.getCaseEnvelopeListByCitizenIdentifier(municipalityId, instanceType, partyId, status, fromDate, toDate))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", NOT_FOUND)
			.hasMessage("Not Found: Citizen identifier not found for partyId: %s".formatted(partyId));

		verify(partyClientMock).getLegalId(municipalityId, partyType, partyId);
		verifyNoMoreInteractions(partyClientMock);
		verifyNoInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
	}

	@Test
	void getCaseStatusByFlowInstanceId() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var name = "name";
		final var expectedCaseStatus = new CaseStatus().withName(name);

		when(openeRestIntegrationMock.getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId))
			.thenReturn(expectedCaseStatus);

		// Act
		final var result = caseService.getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId);

		// Assert
		assertThat(result).isEqualTo(expectedCaseStatus);
		assertThat(result.getName()).isEqualTo(name);

		verify(openeRestIntegrationMock).getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId);
		verifyNoMoreInteractions(openeRestIntegrationMock);
		verifyNoInteractions(openeSoapIntegrationMock, partyClientMock);
	}

	@Test
	void getCaseAttachment() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var queryId = "queryId";
		final var fileId = "fileId";

		final var mockHttpServletResponse = new MockHttpServletResponse();
		final var headers = Map.of(
			"Content-Type", List.of("image/png"),
			"Content-Disposition", List.of("attachment; filename=case-attachment.png"),
			"Content-Length", List.of("0"),
			"Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[0]));
		final var responseEntity = ok()
			.headers(httpHeaders -> httpHeaders.putAll(headers))
			.body(inputStreamResource);

		when(openeRestIntegrationMock.getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId)).thenReturn(responseEntity);

		// Act
		caseService.getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId, mockHttpServletResponse);

		// Assert
		assertThat(mockHttpServletResponse.getContentType()).isEqualTo(IMAGE_PNG_VALUE);
		assertThat(mockHttpServletResponse.getHeader("Content-Disposition")).isEqualTo("attachment; filename=case-attachment.png");
		assertThat(mockHttpServletResponse.getHeader("Content-Length")).isEqualTo("0");
		assertThat(mockHttpServletResponse.getHeader("Last-Modified")).isEqualTo("Wed, 21 Oct 2015 07:28:00 GMT");

		verify(openeRestIntegrationMock).getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId);
		verifyNoMoreInteractions(openeRestIntegrationMock);
		verifyNoInteractions(openeSoapIntegrationMock);
	}

	@Test
	void getCaseAttachmentThrowsException() throws IOException {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "123";
		final var queryId = "queryId";
		final var fileId = "fileId";
		final var mockHttpServletResponse = Mockito.mock(HttpServletResponse.class);
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[0]));
		final var responseEntity = badRequest().body(inputStreamResource);

		when(mockHttpServletResponse.getOutputStream()).thenThrow(new IOException());
		when(openeRestIntegrationMock.getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId)).thenReturn(responseEntity);

		// Act
		assertThatThrownBy(() -> caseService.getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId, mockHttpServletResponse))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", INTERNAL_SERVER_ERROR)
			.hasMessage("Internal Server Error: Unable to get case attachment");

		verify(openeRestIntegrationMock).getCaseAttachment(municipalityId, instanceType, flowInstanceId, queryId, fileId);
		verifyNoMoreInteractions(openeRestIntegrationMock);
		verifyNoInteractions(openeSoapIntegrationMock);
	}
}
