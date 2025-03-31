package se.sundsvall.oepintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

import callback.SetStatusResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

	@Mock
	private OpeneSoapIntegration openeSoapIntegrationMock;

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
}
