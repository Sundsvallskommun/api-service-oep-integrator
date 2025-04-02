package se.sundsvall.oepintegrator.service;

import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

import callback.AddMessageAsOwnerResponse;
import callback.AddMessageResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageAttachmentData;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.integration.party.PartyClient;

@ExtendWith(MockitoExtension.class)
class WebmessageServiceTest {

	@Mock
	private OpeneSoapIntegration openeSoapIntegrationMock;

	@Mock
	private OpeneRestIntegration openeRestIntegrationMock;

	@Mock
	private PartyClient partyClientMock;

	@InjectMocks
	private WebmessageService webmessageService;

	@Test
	void createWebmessageWithUserId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var userId = "userId";
		final var request = WebmessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("1234")))
			.withMessage("message")
			.withSender(Sender.create().withUserId(userId));
		final var files = List.<MultipartFile>of();
		final var response = new AddMessageAsOwnerResponse().withMessageID(1234);

		when(openeSoapIntegrationMock.addMessageAsOwner(eq(municipalityId), eq(instanceType), any())).thenReturn(response);

		// Act
		final var result = webmessageService.createWebmessage(municipalityId, instanceType, request, files);

		// Assert
		assertThat(result).isEqualTo(1234);
		verify(openeSoapIntegrationMock).addMessageAsOwner(eq(municipalityId), eq(instanceType), any());
		verifyNoInteractions(openeRestIntegrationMock, partyClientMock);
		verifyNoMoreInteractions(openeSoapIntegrationMock);
	}

	@Test
	void createWebmessageWithPartyId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var partyId = randomUUID().toString();
		final var legalId = "180101300101";
		final var request = WebmessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("1234")))
			.withMessage("message")
			.withSender(Sender.create().withPartyId(partyId));
		final var files = List.<MultipartFile>of();
		final var response = new AddMessageAsOwnerResponse().withMessageID(1234);

		when(partyClientMock.getLegalId(municipalityId, PRIVATE, partyId)).thenReturn(Optional.of(legalId));
		when(openeSoapIntegrationMock.addMessageAsOwner(eq(municipalityId), eq(instanceType), any())).thenReturn(response);

		// Act
		final var result = webmessageService.createWebmessage(municipalityId, instanceType, request, files);

		// Assert
		assertThat(result).isEqualTo(1234);
		verify(openeSoapIntegrationMock).addMessageAsOwner(eq(municipalityId), eq(instanceType), any());
		verify(partyClientMock).getLegalId(municipalityId, PRIVATE, partyId);
		verifyNoInteractions(openeRestIntegrationMock);
		verifyNoMoreInteractions(openeSoapIntegrationMock, partyClientMock);
	}

	@Test
	void createWebmessageWithAdministratorId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var administratorId = "administratorId";
		final var request = WebmessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("1234")))
			.withMessage("message")
			.withSender(Sender.create().withAdministratorId(administratorId));
		final var files = List.<MultipartFile>of();
		final var response = new AddMessageResponse().withMessageID(1234);

		when(openeSoapIntegrationMock.addMessage(eq(municipalityId), eq(instanceType), any())).thenReturn(response);

		// Act
		final var result = webmessageService.createWebmessage(municipalityId, instanceType, request, files);

		// Assert
		assertThat(result).isEqualTo(1234);
		verify(openeSoapIntegrationMock).addMessage(eq(municipalityId), eq(instanceType), any());
		verifyNoInteractions(openeRestIntegrationMock, partyClientMock);
		verifyNoMoreInteractions(openeSoapIntegrationMock);
	}

	@Test
	void createWebmessageWithPartyIdThrowsExceptionWhenPersonIsMissing() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var partyId = randomUUID().toString();
		final var request = WebmessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("1234")))
			.withMessage("message")
			.withSender(Sender.create().withPartyId(partyId));
		final var files = List.<MultipartFile>of();

		// Act
		final var exception = assertThrows(ThrowableProblem.class, () -> webmessageService.createWebmessage(municipalityId, instanceType, request, files));

		// Assert
		assertThat(exception)
			.isInstanceOf(Problem.class)
			.hasFieldOrPropertyWithValue("status", NOT_FOUND)
			.hasMessage("Not Found: The provided partyId doesn't match any person!");

		verify(partyClientMock).getLegalId(municipalityId, PRIVATE, partyId);
		verifyNoInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
		verifyNoMoreInteractions(partyClientMock);
	}

	@Test
	void createWebmessageWithUserIdThrowsExceptionWhenFlowInstanceIdIsMissing() {
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

		verifyNoInteractions(openeRestIntegrationMock, openeSoapIntegrationMock, partyClientMock);
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
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var attachmentId = 123;
		final var attachment = new WebmessageAttachmentData().withData(new byte[10]);

		when(openeRestIntegrationMock.getAttachmentById(municipalityId, instanceType, attachmentId)).thenReturn(attachment);

		final var result = webmessageService.getAttachmentById(municipalityId, instanceType, attachmentId);

		assertThat(result).isNotNull().isEqualTo(attachment);
		verify(openeRestIntegrationMock).getAttachmentById(municipalityId, instanceType, attachmentId);
		verifyNoMoreInteractions(openeRestIntegrationMock, openeSoapIntegrationMock);
	}
}
