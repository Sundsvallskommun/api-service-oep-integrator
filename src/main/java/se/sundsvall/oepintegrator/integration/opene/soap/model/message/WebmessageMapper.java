package se.sundsvall.oepintegrator.integration.opene.soap.model.message;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.oepintegrator.api.model.webmessage.Direction;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageAttachment;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageAttachmentData;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

public final class WebmessageMapper {

	private WebmessageMapper() {
		// Intentionally Empty
	}

	public static WebmessageAttachmentData toWebmessageAttachmentData(final byte[] data) {
		return WebmessageAttachmentData.create().withData(data);
	}

	public static List<Webmessage> toWebmessages(final String municipalityId, final byte[] messages, final InstanceType instance) {
		return toWebmessages(municipalityId, messages, null, instance);
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
				.map(externalMessage -> toWebmessage(familyId, externalMessage, instance, municipalityId))
				.toList();
		} catch (final Exception e) {
			throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, "%s occurred when parsing open-e messages. Message is: %s".formatted(e.getClass().getSimpleName(), e.getMessage()));
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
			.withSent(ofNullable(externalMessage.getAdded()).map(dateStr -> LocalDateTime.parse(dateStr, OPEN_E_DATE_TIME_FORMAT)).orElse(null));

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
