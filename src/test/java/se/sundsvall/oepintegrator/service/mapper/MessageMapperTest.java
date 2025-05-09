package se.sundsvall.oepintegrator.service.mapper;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;

class MessageMapperTest {

	@Test
	void toAddMessage() {
		// Arrange
		final var sender = Sender.create().withAdministratorId("administratorId");
		final var message = "message";
		final List<MultipartFile> attachments = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));
		final var request = WebmessageRequest.create()
			.withSender(sender)
			.withMessage(message);
		final var flowInstanceId = 123;

		// Act
		final var result = MessageMapper.toAddMessage(request, flowInstanceId, attachments);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(flowInstanceId);
		assertThat(result.getMessage()).isNotNull();
		assertThat(result.getMessage().getMessage()).isEqualTo(message);
		assertThat(result.getMessage().getAttachments()).hasSize(1);
		assertThat(result.getPrincipal()).isNotNull();
		assertThat(result.getPrincipal().getUserID()).isEqualTo(sender.getAdministratorId());
	}

	@Test
	void toAddMessageNullRequest() {
		// Arrange
		final var flowInstanceId = 123;
		final List<MultipartFile> files = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));

		// Act & Assert
		assertThatThrownBy(() -> MessageMapper.toAddMessage(null, flowInstanceId, files))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void toAddMessageNullSender() {
		// Arrange
		final var message = "message";
		final List<MultipartFile> attachments = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));
		final var request = WebmessageRequest.create()
			.withSender(null)
			.withMessage(message);
		final var flowInstanceId = 123;

		// Act & Assert
		assertThatThrownBy(() -> MessageMapper.toAddMessage(request, flowInstanceId, attachments))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Cannot invoke \"se.sundsvall.oepintegrator.api.model.webmessage.Sender.getAdministratorId()\" because the return value of \"se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest.getSender()\" is null");

	}

	@Test
	void toAddMessageAsOwner() {
		// Arrange
		final var sender = Sender.create().withUserId("userId");
		final var message = "message";
		final List<MultipartFile> attachments = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));
		final var request = WebmessageRequest.create()
			.withSender(sender)
			.withMessage(message);
		final var flowInstanceId = 123;

		// Act
		final var result = MessageMapper.toAddMessageAsOwner(request, null, flowInstanceId, attachments);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(flowInstanceId);
		assertThat(result.getMessage()).isNotNull();
		assertThat(result.getMessage().getMessage()).isEqualTo(message);
		assertThat(result.getMessage().getAttachments()).hasSize(1);
		assertThat(result.getPrincipal()).isNotNull();
		assertThat(result.getPrincipal().getUserID()).isEqualTo(sender.getUserId());
		assertThat(result.getPrincipal().getCitizenID()).isNull();
	}

	@Test
	void toAddMessageAsOwnerWithLegalId() {
		// Arrange
		final var sender = Sender.create().withPartyId(randomUUID().toString());
		final var legalId = "18010130-0101";
		final var message = "message";
		final List<MultipartFile> attachments = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));
		final var request = WebmessageRequest.create()
			.withSender(sender)
			.withMessage(message);
		final var flowInstanceId = 123;

		// Act
		final var result = MessageMapper.toAddMessageAsOwner(request, legalId, flowInstanceId, attachments);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(flowInstanceId);
		assertThat(result.getMessage()).isNotNull();
		assertThat(result.getMessage().getMessage()).isEqualTo(message);
		assertThat(result.getMessage().getAttachments()).hasSize(1);
		assertThat(result.getPrincipal()).isNotNull();
		assertThat(result.getPrincipal().getUserID()).isNull();
		assertThat(result.getPrincipal().getCitizenID()).isEqualTo(legalId);
	}

	@Test
	void toAddMessageAsOwnerNullRequest() {
		// Arrange
		final var flowInstanceId = 123;
		final List<MultipartFile> files = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));

		// Act & Assert
		assertThatThrownBy(() -> MessageMapper.toAddMessageAsOwner(null, null, flowInstanceId, files))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void toAddMessageAsOwnerNullSender() {
		// Arrange
		final var message = "message";
		final List<MultipartFile> attachments = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));
		final var request = WebmessageRequest.create()
			.withSender(null)
			.withMessage(message);
		final var flowInstanceId = 123;

		// Act & Assert
		assertThatThrownBy(() -> MessageMapper.toAddMessageAsOwner(request, null, flowInstanceId, attachments))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Cannot invoke \"se.sundsvall.oepintegrator.api.model.webmessage.Sender.getUserId()\" because the return value of \"se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest.getSender()\" is null");
	}

	@Test
	void toAttachments() {
		// Arrange
		final List<MultipartFile> attachments = List.of(new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes()));

		// Act
		final var result = MessageMapper.toAttachments(attachments);

		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst().getFilename()).isEqualTo("file.txt");
		assertThat(result.getFirst().getSize()).isEqualTo(12);
	}

	@Test
	void toAttachmentsNullAttachments() {
		// Act
		final var result = MessageMapper.toAttachments(null);

		// Assert
		assertThat(result).isNotNull().isEmpty();
	}

	@Test
	void toAttachment() {
		// Arrange
		final var attachment = new MockMultipartFile("attachment", "file.txt", "text/plain", "some content".getBytes());

		// Act
		final var result = MessageMapper.toAttachment(attachment);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFilename()).isEqualTo("file.txt");
		assertThat(result.getSize()).isEqualTo(12);
		assertThat(result.getEncodedData()).isEqualTo("some content".getBytes());
	}

	@Test
	void toAttachmentNullAttachment() {
		// Act & Assert
		assertThatThrownBy(() -> MessageMapper.toAttachment(null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void toAttachmentIoException() {
		// Arrange
		final var attachment = new MockMultipartFile("attachment", "file.txt", "text/plain", new byte[0]) {
			@NotNull
			@Override
			public byte[] getBytes() throws IOException {
				throw new IOException("Failed to read attachment");
			}
		};

		// Act & Assert
		assertThatThrownBy(() -> MessageMapper.toAttachment(attachment))
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Failed to read attachment")
			.hasFieldOrPropertyWithValue("status", INTERNAL_SERVER_ERROR);
	}
}
