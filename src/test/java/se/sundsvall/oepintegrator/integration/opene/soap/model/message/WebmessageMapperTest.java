package se.sundsvall.oepintegrator.integration.opene.soap.model.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.webmessage.Direction;
import se.sundsvall.oepintegrator.util.enums.InstanceType;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ResourceLoaderExtension.class)
class WebmessageMapperTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String FAMILY_ID = "12345";
	private static final InstanceType INSTANCE_TYPE = InstanceType.EXTERNAL;

	@Test
	void toWebmessages(@Load("/mappings/messages.xml") final String xml) {

		// Act
		final var result = WebmessageMapper.toWebmessages(MUNICIPALITY_ID, xml.getBytes(ISO_8859_1), FAMILY_ID, INSTANCE_TYPE);

		// Assert
		assertThat(result)
			.hasSize(1)
			.first()
			.satisfies(message -> {
				assertThat(message.getFamilyId()).isEqualTo(FAMILY_ID);
				assertThat(message.getInstance()).isEqualTo(INSTANCE_TYPE.name());
				assertThat(message.getDirection()).isEqualTo(Direction.INBOUND);
				assertThat(message.getMessageId()).isEqualTo("143750");
				assertThat(message.getExternalCaseId()).isEqualTo("1234");
				assertThat(message.getMessage()).isEqualTo("Test message");
				assertThat(message.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
				assertThat(message.getUserId()).isEqualTo("123");
				assertThat(message.getUsername()).isEqualTo("testuser");
				assertThat(message.getFirstName()).isEqualTo("Test");
				assertThat(message.getLastName()).isEqualTo("User");
				assertThat(message.getEmail()).isEqualTo("test@example.com");
				assertThat(message.getAttachments())
					.hasSize(1)
					.first()
					.satisfies(attachment -> {
						assertThat(attachment.getAttachmentId()).isEqualTo(1);
						assertThat(attachment.getName()).isEqualTo("test.pdf");
						assertThat(attachment.getExtension()).isEqualTo("pdf");
					});
			});
	}

	@Test
	void toWebmessages_withNullInput_returnsEmptyList() {
		// Act
		final var result = WebmessageMapper.toWebmessages(MUNICIPALITY_ID, null, FAMILY_ID, INSTANCE_TYPE);

		// Assert
		assertThat(result).isEmpty();
	}

	@Test
	void toWebmessages_withInvalidXml_throwsProblem() {
		// Arrange
		final String invalidXml = "Invalid XML content";

		// Act & Assert

		assertThatThrownBy(() -> WebmessageMapper.toWebmessages(MUNICIPALITY_ID, invalidXml.getBytes(ISO_8859_1), FAMILY_ID, INSTANCE_TYPE))
			.isInstanceOf(Problem.class)
			.hasMessageStartingWith("Internal Server Error: StreamReadException occurred when parsing open-e messages. Message is: Unexpected character 'I' (code 73) in prolog; expected '<'");
	}

	@Test
	void toWebmessagesWithEmptyMessagesReturnsEmptyList(@Load("/mappings/empty-messages.xml") final String xml) {
		// Act
		final var result = WebmessageMapper.toWebmessages(MUNICIPALITY_ID, xml.getBytes(ISO_8859_1), FAMILY_ID, INSTANCE_TYPE);

		// Assert
		assertThat(result).isEmpty();
	}
}
