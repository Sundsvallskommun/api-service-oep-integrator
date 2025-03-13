package se.sundsvall.oepintegrator.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static se.sundsvall.oepintegrator.utility.Constants.REFERENCE_FLOW_INSTANCE_ID;

import callback.AddMessageResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.model.webmessage.WebMessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.service.WebMessageService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class WebMessageResourceTest {

	private static final String PATH = "/{municipalityId}/{instanceType}/webmessages";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private WebMessageService webMessageService;

	@Test
	void createWebMessage() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var sender = Sender.create().withUserId("userId");
		final var message = "message";
		final var request = WebMessageRequest.create()
			.withSender(sender)
			.withExternalReferences(List.of(ExternalReference.create().withKey(REFERENCE_FLOW_INSTANCE_ID).withValue("123")))
			.withMessage(message);

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("files", "file").filename("file.txt").contentType(TEXT_PLAIN);
		multipartBodyBuilder.part("files", "file").filename("file2.txt").contentType(TEXT_PLAIN);
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var messageId = 1234;
		final var response = new AddMessageResponse()
			.withMessageID(messageId);

		when(webMessageService.createWebMessage(eq(municipalityId), eq(instanceType), eq(request), any())).thenReturn(response);

		// Act
		webTestClient.post()
			.uri(builder -> builder.path(PATH).build(Map.of("municipalityId", municipalityId, "instanceType", instanceType)))
			.contentType(MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL)
			.expectHeader().location("/" + municipalityId + "/webmessages/" + messageId)
			.expectBody().isEmpty();

		// Assert
		verify(webMessageService).createWebMessage(eq(municipalityId), eq(instanceType), eq(request), any());
		verifyNoMoreInteractions(webMessageService);
	}
}
