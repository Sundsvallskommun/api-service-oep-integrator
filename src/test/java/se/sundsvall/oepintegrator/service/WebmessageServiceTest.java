package se.sundsvall.oepintegrator.service;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

import callback.AddMessageResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;

@ExtendWith(MockitoExtension.class)
class WebmessageServiceTest {

	@Mock
	private OpeneSoapIntegration openeSoapIntegrationMock;

	@Mock
	private OpeneRestIntegration openeRestIntegrationMock;

	@InjectMocks
	private WebmessageService webmessageService;

	@Test
	void createWebmessage() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var userId = "userId";
		final var request = WebmessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("1234")))
			.withMessage("message")
			.withSender(Sender.create().withUserId(userId));
		final var files = List.<MultipartFile>of();
		final var response = new AddMessageResponse().withMessageID(1234);

		when(openeSoapIntegrationMock.addMessage(eq(municipalityId), eq(instanceType), any())).thenReturn(response);

		// Act
		final var result = webmessageService.createWebmessage(municipalityId, instanceType, request, files);

		// Assert
		assertThat(result).isEqualTo(1234);
		verify(openeSoapIntegrationMock).addMessage(eq(municipalityId), eq(instanceType), any());
		verifyNoMoreInteractions(openeSoapIntegrationMock, openeRestIntegrationMock);
	}

	@Test
	void createWebmessageThrowsExceptionWhenFlowInstanceIdIsMissing() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var userId = "userId";
		final var request = WebmessageRequest.create()
			.withMessage("message")
			.withSender(Sender.create().withUserId(userId));
		final var files = List.<MultipartFile>of();

		// Act & Assert
		assertThatThrownBy(() -> webmessageService.createWebmessage(municipalityId, instanceType, request, files))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", BAD_REQUEST)
			.hasMessage("Bad Request: Flow instance id is required");

		verifyNoMoreInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
	}

	@Test
	void getWebmessagesByFamilyId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var fromDate = now();
		final var toDate = now();

		final var webmessages = List.of(
			new Webmessage().withMessageId("2"),
			new Webmessage().withMessageId("1"));
		when(openeRestIntegrationMock.getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDate, toDate)).thenReturn(webmessages);
		// Act
		final var result = webmessageService.getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDate, toDate);

		// Assert
		assertThat(result).isNotNull().hasSize(2);
		assertThat(result.getFirst().getMessageId()).isEqualTo("2");
		verify(openeRestIntegrationMock).getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDate, toDate);
		verifyNoMoreInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
	}

	@Test
	void getWebmessageByFlowInstanceId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "flowInstanceId";
		final var fromDate = now();
		final var toDate = now();

		final var webmessages = List.of(
			new Webmessage().withMessageId("2"),
			new Webmessage().withMessageId("1"));
		when(openeRestIntegrationMock.getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDate, toDate)).thenReturn(webmessages);
		// Act
		final var result = webmessageService.getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDate, toDate);

		// Assert
		assertThat(result).isNotNull().hasSize(2);
		assertThat(result.getFirst().getMessageId()).isEqualTo("2");
		verify(openeRestIntegrationMock).getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDate, toDate);
		verifyNoMoreInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
	}

	@Test
	void getAttachmentById() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var attachmentId = 123;

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

		when(openeRestIntegrationMock.getAttachmentById(municipalityId, instanceType, attachmentId)).thenReturn(responseEntity);

		// Act
		webmessageService.getAttachmentById(municipalityId, instanceType, attachmentId, mockHttpServletResponse);

		// Assert
		assertThat(mockHttpServletResponse.getContentType()).isEqualTo("application/pdf");
		assertThat(mockHttpServletResponse.getHeader("Content-Disposition")).isEqualTo("attachment; filename=case.pdf");
		assertThat(mockHttpServletResponse.getHeader("Content-Length")).isEqualTo("0");
		assertThat(mockHttpServletResponse.getHeader("Last-Modified")).isEqualTo("Wed, 21 Oct 2015 07:28:00 GMT");
		verify(openeRestIntegrationMock).getAttachmentById(municipalityId, instanceType, attachmentId);
		verifyNoMoreInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
	}

	@Test
	void getAttachmentByIdThrowsException() throws IOException {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var attachmentId = 123;
		final var mockHttpServletResponse = mock(HttpServletResponse.class);
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[10]));
		final var responseEntity = ResponseEntity.badRequest().body(inputStreamResource);
		when(mockHttpServletResponse.getOutputStream()).thenThrow(new IOException());

		when(openeRestIntegrationMock.getAttachmentById(municipalityId, instanceType, attachmentId)).thenReturn(responseEntity);

		// Act & Assert
		assertThatThrownBy(() -> webmessageService.getAttachmentById(municipalityId, instanceType, attachmentId, mockHttpServletResponse))
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", INTERNAL_SERVER_ERROR)
			.hasMessage("Internal Server Error: Unable to get attachment by ID");

		verifyNoMoreInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
	}
}
