package se.sundsvall.oepintegrator.integration.opene.rest.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

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
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageAttachmentData;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestClient;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;

@ExtendWith({
	MockitoExtension.class, ResourceLoaderExtension.class
})
class OpeneRestIntegrationTest {

	@Mock
	private OpeneRestClient openeRestClient;
	@Mock
	private OpeneClientFactory clientFactory;

	@InjectMocks
	private OpeneRestIntegration openeRestIntegration;

	@Test
	void getWebmessagesByFamilyId(@Load("/mappings/messages.xml") final String xml) {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getWebmessagesByFamilyId(familyId, formattedFromDateTime, formattedToDateTime)).thenReturn(xml.getBytes());

		// Act
		final var result = openeRestIntegration.getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDateTime, toDateTime);

		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst().getMessage()).startsWith("Test message");
		assertThat(result.getFirst().getDirection()).isEqualTo(Direction.INBOUND);

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getWebmessagesByFamilyId(familyId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getWebmessagesByFamilyIdNullResult() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getWebmessagesByFamilyId(familyId, formattedFromDateTime, formattedToDateTime)).thenReturn(null);

		// Act
		final var result = openeRestIntegration.getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDateTime, toDateTime);

		// Assert
		assertThat(result).isNotNull().isEmpty();

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getWebmessagesByFamilyId(familyId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getWebmessagesByFamilyIdParsingError() {
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getWebmessagesByFamilyId(familyId, formattedFromDateTime, formattedToDateTime)).thenReturn(new byte[0]);

		// Act & Assert
		assertThatThrownBy(() -> openeRestIntegration.getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDateTime, toDateTime))
			.isInstanceOf(Problem.class)
			.hasMessageStartingWith("Internal Server Error: JsonParseException occurred when parsing open-e messages. Message is: Unexpected EOF in prolog");

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getWebmessagesByFamilyId(familyId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getWebmessagesByFlowInstanceId(@Load("/mappings/messages.xml") final String xml) {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "flowInstanceId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getWebmessagesByFlowInstanceId(flowInstanceId, formattedFromDateTime, formattedToDateTime)).thenReturn(xml.getBytes());

		// Act
		final var result = openeRestIntegration.getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDateTime, toDateTime);

		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst().getMessage()).startsWith("Test message");
		assertThat(result.getFirst().getDirection()).isEqualTo(Direction.INBOUND);

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getWebmessagesByFlowInstanceId(flowInstanceId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getWebmessagesByFlowInstanceIdNullResult() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "flowInstanceId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getWebmessagesByFlowInstanceId(flowInstanceId, formattedFromDateTime, formattedToDateTime)).thenReturn(null);

		// Act
		final var result = openeRestIntegration.getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDateTime, toDateTime);

		// Assert
		assertThat(result).isNotNull().isEmpty();

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getWebmessagesByFlowInstanceId(flowInstanceId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getWebmessagesByFlowInstanceIdParsingError() {
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "flowInstanceId";
		final var fromDateTime = LocalDateTime.now();
		final var toDateTime = LocalDateTime.now();
		final var formattedFromDateTime = fromDateTime.format(OPEN_E_DATE_TIME_FORMAT);
		final var formattedToDateTime = toDateTime.format(OPEN_E_DATE_TIME_FORMAT);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getWebmessagesByFlowInstanceId(flowInstanceId, formattedFromDateTime, formattedToDateTime)).thenReturn(new byte[0]);

		// Act & Assert
		assertThatThrownBy(() -> openeRestIntegration.getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDateTime, toDateTime))
			.isInstanceOf(Problem.class)
			.hasMessageStartingWith("Internal Server Error: JsonParseException occurred when parsing open-e messages. Message is: Unexpected EOF in prolog");

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getWebmessagesByFlowInstanceId(flowInstanceId, formattedFromDateTime, formattedToDateTime);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getAttachmentById() {
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var attachmentId = 123;
		final var bytes = new byte[10];
		final var attachment = new WebmessageAttachmentData().withData(bytes);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getAttachmentById(attachmentId)).thenReturn(bytes);

		final var result = openeRestIntegration.getAttachmentById(municipalityId, instanceType, attachmentId);

		assertThat(result).isNotNull().isEqualTo(attachment);
		verify(openeRestClient).getAttachmentById(attachmentId);
		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}
}
