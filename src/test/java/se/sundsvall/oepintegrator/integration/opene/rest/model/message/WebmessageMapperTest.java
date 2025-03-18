package se.sundsvall.oepintegrator.integration.opene.rest.model.message;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.Direction;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.opene.soap.model.message.WebmessageMapper;

class WebmessageMapperTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String FAMILY_ID = "12345";
	private static final InstanceType INSTANCE_TYPE = InstanceType.EXTERNAL;

	@Test
	void toWebmessages_success() {
		// Arrange
		final String xml = """
			<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
			<Messages>
			<ExternalMessage>
			    <postedByManager>false</postedByManager>
			    <systemMessage>false</systemMessage>
			    <readReceiptEnabled>false</readReceiptEnabled>
			    <messageID>143750</messageID>
			    <message>Test message</message>
			    <poster>
			        <userID>123</userID>
			        <username>testuser</username>
			        <firstname>Test</firstname>
			        <lastname>User</lastname>
			        <email>test@example.com</email>
			        <admin>false</admin>
			        <enabled>true</enabled>
			        <lastLogin>2025-03-04 20:54</lastLogin>
			        <lastLoginInMilliseconds>1741118055000</lastLoginInMilliseconds>
			        <added>2017-09-25 09:04</added>
			        <isMutable>true</isMutable>
			        <hasFormProvider>true</hasFormProvider>
			    </poster>
			    <added>2025-03-03 10:41</added>
			    <flowInstanceID>1234</flowInstanceID>
			     <attachments>
			            <ExternalMessageAttachment>
			                <attachmentID>1</attachmentID>
			                <filename>test.pdf</filename>
			                <size>5916680</size>
			                <added>2025-03-03 15:08</added>
			                <FormatedSize>5,64 MB</FormatedSize>
			            </ExternalMessageAttachment>
			        </attachments>
			</ExternalMessage>
			</Messages>
			""";

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
			.hasMessageStartingWith("Internal Server Error: JsonParseException occurred when parsing open-e messages. Message is: Unexpected character 'I' (code 73) in prolog; expected '<'");
	}

	@Test
	void toWebmessages_withEmptyMessages_returnsEmptyList() {
		// Arrange
		final String xml = """
			<?xml version="1.0" encoding="ISO-8859-1"?>
			<messages>
			    <externalMessages/>
			</messages>
			""";

		// Act
		final var result = WebmessageMapper.toWebmessages(MUNICIPALITY_ID, xml.getBytes(ISO_8859_1), FAMILY_ID, INSTANCE_TYPE);

		// Assert
		assertThat(result).isEmpty();
	}

	@Test
	void toWebmessages_filtersOutManagerMessages() {
		// Arrange
		final String xml = """
			<?xml version="1.0" encoding="ISO-8859-1"?>
			<messages>
			    <externalMessages>
			        <externalMessage>
			            <messageID>1001</messageID>
			            <postedByManager>true</postedByManager>
			        </externalMessage>
			    </externalMessages>
			</messages>
			""";

		// Act
		final var result = WebmessageMapper.toWebmessages(MUNICIPALITY_ID, xml.getBytes(ISO_8859_1), FAMILY_ID, INSTANCE_TYPE);

		// Assert
		assertThat(result).isEmpty();
	}
}
