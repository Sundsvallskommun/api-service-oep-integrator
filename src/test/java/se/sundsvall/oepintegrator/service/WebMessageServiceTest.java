package se.sundsvall.oepintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.BAD_REQUEST;

import callback.AddMessageResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.WebMessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;

@ExtendWith(MockitoExtension.class)
class WebMessageServiceTest {

	@Mock
	private OpeneSoapIntegration openeSoapIntegrationMock;

	@InjectMocks
	private WebMessageService webMessageService;

	@Test
	void createWebMessage() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var userId = "userId";
		final var request = WebMessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("1234")))
			.withMessage("message")
			.withSender(Sender.create().withUserId(userId));
		final var files = List.<MultipartFile>of();
		final var response = new AddMessageResponse().withMessageID(1234);

		when(openeSoapIntegrationMock.addMessage(eq(municipalityId), eq(instanceType), any())).thenReturn(response);

		// Act
		final var result = webMessageService.createWebMessage(municipalityId, instanceType, request, files);

		// Assert
		assertThat(result).isEqualTo(1234);
		verify(openeSoapIntegrationMock).addMessage(eq(municipalityId), eq(instanceType), any());
		verifyNoMoreInteractions(openeSoapIntegrationMock);
	}

	@Test
	void createWebMessageThrowsExceptionWhenFlowInstanceIdIsMissing() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var userId = "userId";
		final var request = WebMessageRequest.create()
			.withMessage("message")
			.withSender(Sender.create().withUserId(userId));
		final var files = List.<MultipartFile>of();

		// Act & Assert
		assertThatThrownBy(() -> webMessageService.createWebMessage(municipalityId, instanceType, request, files))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", BAD_REQUEST)
			.hasMessage("Bad Request: Flow instance id is required");

		verifyNoMoreInteractions(openeSoapIntegrationMock);
	}
}
