package se.sundsvall.oepintegrator.integration.opene.soap.model.message;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.oepintegrator.api.model.webmessage.Direction;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageAttachment;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;

public final class WebmessageMapper {

	private static final Logger LOG = LoggerFactory.getLogger(WebmessageMapper.class);

	private WebmessageMapper() {
		// Intentionally Empty
	}

	public static List<Webmessage> toWebmessages(final String municipalityId, final byte[] messages, final String familyId, final InstanceType instance) {

		if (messages == null) {
			return emptyList();
		}

		final var xmlString = new String(messages, StandardCharsets.ISO_8859_1);
		try {
			return ofNullable(new XmlMapper().readValue(xmlString, Messages.class).getExternalMessages())
				.orElse(emptyList())
				.stream()
				.filter(externalMessage -> !externalMessage.isPostedByManager())
				.map(externalMessage -> toWebmessage(familyId, externalMessage, instance, municipalityId))
				.toList();
		} catch (final Exception e) {
			throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, "%s occurred when parsing open-e messages for familyId %s. Message is: %s".formatted(e.getClass().getSimpleName(), familyId, e.getMessage()));
		}
	}

	private static Webmessage toWebmessage(final String familyId, final ExternalMessage externalMessage, final InstanceType instance, final String municipalityId) {

		final var webmessage = Webmessage.create()
			.withFamilyId(familyId)
			.withInstance(instance.name())
			.withDirection(externalMessage.isPostedByManager() ? Direction.OUTBOUND : Direction.INBOUND)
			.withMessageId(String.valueOf(externalMessage.getMessageID()))
			.withExternalCaseId(String.valueOf(externalMessage.getFlowInstanceID()))
			.withMessage(externalMessage.getMessage())
			.withMunicipalityId(municipalityId)
			.withSent(externalMessage.getAdded());

		ofNullable(externalMessage.getPoster())
			.ifPresent(poster -> webmessage
				.withEmail(poster.getEmail())
				.withFirstName(poster.getFirstname())
				.withLastName(poster.getLastname())
				.withUsername(poster.getUsername())
				.withUserId(String.valueOf(poster.getUserID())));

		ofNullable(externalMessage.getAttachments())
			.ifPresent(attachments -> webmessage
				.setAttachments(attachments.stream()
					.map(attachment -> WebmessageAttachment.create()
						.withAttachmentId(attachment.getAttachmentID())
						.withName(attachment.getFileName())
						.withMimeType(getMimeType(attachment.getFileName()))
						.withExtension(getFileExtension(attachment.getFileName())))
					.toList()));

		return webmessage;
	}

	private static String getMimeType(final String file) {
		try {
			final String encodedFile = URLEncoder.encode(file, StandardCharsets.ISO_8859_1);
			return Files.probeContentType(Paths.get(new URI("file:///" + encodedFile)));
		} catch (final IOException | URISyntaxException e) {
			throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, "Unable to determine mime type for file %s".formatted(file));
		}

	}

	private static String getFileExtension(final String file) {
		final int dotIndex = file.lastIndexOf('.');
		return (dotIndex == -1) ? "" : file.substring(dotIndex + 1);
	}

}
