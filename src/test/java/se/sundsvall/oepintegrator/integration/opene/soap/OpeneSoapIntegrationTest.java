package se.sundsvall.oepintegrator.integration.opene.soap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import callback.AddMessage;
import callback.AddMessageResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Problem;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.webmessage.Direction;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;

@ExtendWith({
	MockitoExtension.class, ResourceLoaderExtension.class
})
class OpeneSoapIntegrationTest {

	@Mock
	private OpeneSoapClient openeSoapClient;
	@Mock
	private OpeneClientFactory clientFactory;

	@InjectMocks
	private OpeneSoapIntegration openeSoapIntegration;

	@Test
	void addMessage() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var addMessage = new AddMessage();

		final var addMessageResponse = new AddMessageResponse();
		when(openeSoapClient.addMessage(addMessage)).thenReturn(addMessageResponse);

		when(clientFactory.getSoapClient(municipalityId, instanceType)).thenReturn(openeSoapClient);

		// Act
		final var result = openeSoapIntegration.addMessage(municipalityId, instanceType, addMessage);

		// Assert
		assertThat(result).isNotNull().isEqualTo(addMessageResponse);
		verify(clientFactory).getSoapClient(municipalityId, instanceType);
		verify(openeSoapClient).addMessage(addMessage);
		verifyNoMoreInteractions(openeSoapClient, clientFactory);
	}

	@Test
	void getWebmessages(@Load("/mappings/messages.xml") final String xml) {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var familyId = "familyId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getSoapClient(municipalityId, instanceType)).thenReturn(openeSoapClient);
		when(openeSoapClient.getMessages(familyId, formattedFromDateTime, formattedToDateTime)).thenReturn(xml.getBytes());

		// Act
		final var result = openeSoapIntegration.getMessages(municipalityId, instanceType, familyId, fromDateTime, toDateTime);

		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst().getMessage()).startsWith("Test message");
		assertThat(result.getFirst().getDirection()).isEqualTo(Direction.INBOUND);

		verify(clientFactory).getSoapClient(municipalityId, instanceType);
		verify(openeSoapClient).getMessages(familyId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeSoapClient, clientFactory);
	}

	@Test
	void getWebmessagesNullResult() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var familyId = "familyId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getSoapClient(municipalityId, instanceType)).thenReturn(openeSoapClient);
		when(openeSoapClient.getMessages(familyId, formattedFromDateTime, formattedToDateTime)).thenReturn(null);

		// Act
		final var result = openeSoapIntegration.getMessages(municipalityId, instanceType, familyId, fromDateTime, toDateTime);

		// Assert
		assertThat(result).isNotNull().isEmpty();

		verify(clientFactory).getSoapClient(municipalityId, instanceType);
		verify(openeSoapClient).getMessages(familyId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeSoapClient, clientFactory);
	}

	@Test
	void getWebmessagesParsingError() {
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var familyId = "familyId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getSoapClient(municipalityId, instanceType)).thenReturn(openeSoapClient);
		when(openeSoapClient.getMessages(familyId, formattedFromDateTime, formattedToDateTime)).thenReturn(new byte[0]);

		// Act & Assert
		assertThatThrownBy(() -> openeSoapIntegration.getMessages(municipalityId, instanceType, familyId, fromDateTime, toDateTime))
			.isInstanceOf(Problem.class)
			.hasMessageStartingWith("Internal Server Error: JsonParseException occurred when parsing open-e messages for familyId familyId. Message is: Unexpected EOF in prolog");

		verify(clientFactory).getSoapClient(municipalityId, instanceType);
		verify(openeSoapClient).getMessages(familyId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeSoapClient, clientFactory);

	}
}
