package se.sundsvall.oepintegrator.service.mapper;

import static java.util.Collections.emptyList;
import static javax.xml.datatype.DatatypeFactory.newInstance;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import callback.AddMessage;
import callback.Attachment;
import callback.IntegrationMessage;
import callback.Principal;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.WebMessageRequest;

public final class MessageMapper {

	private MessageMapper() {
		// Tp prevent instantiation
	}

	public static AddMessage toAddMessage(final WebMessageRequest request, final Integer flowInstanceId, final List<MultipartFile> attachments) {

		final IntegrationMessage integrationMessage;
		try {
			integrationMessage = new IntegrationMessage()
				.withAdded(newInstance().newXMLGregorianCalendar(new GregorianCalendar()))
				.withAttachments(toAttachments(attachments))
				.withMessage(request.getMessage());
		} catch (final DatatypeConfigurationException e) {
			throw Problem.valueOf(INTERNAL_SERVER_ERROR, "Failed to create message");
		}

		return new AddMessage()
			.withFlowInstanceID(flowInstanceId)
			.withMessage(integrationMessage)
			.withPrincipal(Optional.ofNullable(request.getSender().getUserId())
				.map(value -> new Principal().withUserID(value))
				.orElse(null));

	}

	static List<Attachment> toAttachments(final List<MultipartFile> attachments) {
		return Optional.ofNullable(attachments).orElse(emptyList()).stream()
			.map(MessageMapper::toAttachment)
			.toList();
	}

	static Attachment toAttachment(final MultipartFile attachment) {

		try {
			return new Attachment()
				.withEncodedData(attachment.getBytes())
				.withFilename(attachment.getOriginalFilename())
				.withSize(attachment.getSize());
		} catch (final IOException e) {
			throw Problem.valueOf(INTERNAL_SERVER_ERROR, "Failed to read attachment");
		}
	}
}
