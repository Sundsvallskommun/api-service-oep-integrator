package se.sundsvall.oepintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

import callback.SetStatusResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

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
		final var request = new SetStatusRequest().withStatus("status").withPrincipal(new Principal().withName("name").withUserId("userId"));
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
		final var request = new SetStatusRequest().withStatus("status").withPrincipal(new Principal().withName("name").withUserId("userId"));
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
			"Content-Type", List.of("application/pdf"),
			"Content-Disposition", List.of("attachment; filename=case.pdf"),
			"Content-Length", List.of("0"),
			"Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[10]));
		final var responseEntity = ResponseEntity.ok()
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
		final var responseEntity = ResponseEntity.badRequest().body(inputStreamResource);
		when(mockHttpServletResponse.getOutputStream()).thenThrow(new IOException());

		when(openeRestIntegrationMock.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId)).thenReturn(responseEntity);

		// Act
		assertThatThrownBy(() -> caseService.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId, mockHttpServletResponse))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", INTERNAL_SERVER_ERROR)
			.hasMessage("Internal Server Error: Unable to get case pdf");
	}
}
