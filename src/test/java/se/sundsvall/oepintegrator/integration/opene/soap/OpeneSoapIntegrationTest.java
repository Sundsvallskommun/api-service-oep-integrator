package se.sundsvall.oepintegrator.integration.opene.soap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;

import callback.AddMessage;
import callback.AddMessageAsOwner;
import callback.AddMessageAsOwnerResponse;
import callback.AddMessageResponse;
import callback.ConfirmDelivery;
import callback.SetStatus;
import callback.SetStatusResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
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
	void confirmDelivery() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var confirmDelivery = new ConfirmDelivery();

		when(clientFactory.getSoapClient(municipalityId, instanceType)).thenReturn(openeSoapClient);

		// Act
		openeSoapIntegration.confirmDelivery(municipalityId, instanceType, confirmDelivery);

		// Assert
		verify(clientFactory).getSoapClient(municipalityId, instanceType);
		verify(openeSoapClient).confirmDelivery(confirmDelivery);
		verifyNoMoreInteractions(openeSoapClient, clientFactory);
	}

	@Test
	void addMessage() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
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
	void addMessageAsOwner() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var addMessageAsOwner = new AddMessageAsOwner();

		final var addMessageAsOwnerResponse = new AddMessageAsOwnerResponse();
		when(openeSoapClient.addMessageAsOwner(addMessageAsOwner)).thenReturn(addMessageAsOwnerResponse);

		when(clientFactory.getSoapClient(municipalityId, instanceType)).thenReturn(openeSoapClient);

		// Act
		final var result = openeSoapIntegration.addMessageAsOwner(municipalityId, instanceType, addMessageAsOwner);

		// Assert
		assertThat(result).isNotNull().isEqualTo(addMessageAsOwnerResponse);
		verify(clientFactory).getSoapClient(municipalityId, instanceType);
		verify(openeSoapClient).addMessageAsOwner(addMessageAsOwner);
		verifyNoMoreInteractions(openeSoapClient, clientFactory);
	}

	@Test
	void setStatus() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var setStatus = new SetStatus();

		final var setStatusResponse = new SetStatusResponse();

		when(openeSoapClient.setStatus(setStatus)).thenReturn(setStatusResponse);

		when(clientFactory.getSoapClient(municipalityId, instanceType)).thenReturn(openeSoapClient);

		// Act
		final var result = openeSoapIntegration.setStatus(municipalityId, instanceType, setStatus);

		// Assert
		assertThat(result).isNotNull().isEqualTo(setStatusResponse);
		verify(clientFactory).getSoapClient(municipalityId, instanceType);
		verify(openeSoapClient).setStatus(setStatus);
		verifyNoMoreInteractions(openeSoapClient, clientFactory);
	}
}
