package se.sundsvall.oepintegrator.service.mapper;

import callback.AddMessage;
import callback.AddMessageAsOwner;
import callback.Attachment;
import callback.IntegrationMessage;
import callback.OwnerPrincipal;
import callback.Principal;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;

import static java.util.Collections.emptyList;
import static javax.xml.datatype.DatatypeFactory.newInstance;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

public final class MessageMapper {

	private MessageMapper() {}

	public static AddMessage toAddMessage(final WebmessageRequest request, final Integer flowInstanceId, final List<MultipartFile> attachments) {
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
			.withPrincipal(Optional.ofNullable(request.getSender())
				.map(Sender::getAdministratorId)
				.map(value -> new Principal().withUserID(value))
				.orElse(null));

	}

	public static AddMessageAsOwner toAddMessageAsOwner(final WebmessageRequest request, final String legalId, final Integer flowInstanceId, final List<MultipartFile> attachments) {
		final IntegrationMessage integrationMessage;
		try {
			integrationMessage = new IntegrationMessage()
				.withAdded(newInstance().newXMLGregorianCalendar(new GregorianCalendar()))
				.withAttachments(toAttachments(attachments))
				.withMessage(request.getMessage());
		} catch (final DatatypeConfigurationException e) {
			throw Problem.valueOf(INTERNAL_SERVER_ERROR, "Failed to create message");
		}

		return new AddMessageAsOwner()
			.withFlowInstanceID(flowInstanceId)
			.withMessage(integrationMessage)
			.withPrincipal(new OwnerPrincipal()
				.withUserID(request.getSender().getUserId())
				.withCitizenID(legalId));
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
