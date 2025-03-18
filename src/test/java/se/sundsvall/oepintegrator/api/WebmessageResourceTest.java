package se.sundsvall.oepintegrator.api;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
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

import java.time.ZoneId;
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
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.service.WebmessageService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class WebmessageResourceTest {

	private static final String PATH = "/{municipalityId}/{instanceType}/webmessages";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private WebmessageService webmessageService;

	@Test
	void createWebmessage() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var sender = Sender.create().withUserId("userId");
		final var message = "message";
		final var request = WebmessageRequest.create()
			.withSender(sender)
			.withExternalReferences(List.of(ExternalReference.create().withKey(REFERENCE_FLOW_INSTANCE_ID).withValue("123")))
			.withMessage(message);

		final var multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("files", "file").filename("file.txt").contentType(TEXT_PLAIN);
		multipartBodyBuilder.part("files", "file").filename("file2.txt").contentType(TEXT_PLAIN);
		multipartBodyBuilder.part("request", request, APPLICATION_JSON);
		final var body = multipartBodyBuilder.build();

		final var messageId = 1234;

		when(webmessageService.createWebmessage(eq(municipalityId), eq(instanceType), eq(request), any())).thenReturn(messageId);

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
		verify(webmessageService).createWebmessage(eq(municipalityId), eq(instanceType), eq(request), any());
		verifyNoMoreInteractions(webmessageService);
	}

	@Test
	void getWebmessages() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var familyId = "123";
		final var fromDateTime = now(ZoneId.systemDefault()).minusDays(4);
		final var toDateTime = now(ZoneId.systemDefault());
		final var webmessage = Webmessage.create().withId(123).withMessage("message");

		when(webmessageService.getWebmessages(municipalityId, instanceType, familyId, fromDateTime, toDateTime)).thenReturn(List.of(webmessage));
		// Act
		final var result = webTestClient.get()
			.uri(builder -> builder.path(PATH + "/{familyId}")
				.queryParam("fromDateTime", fromDateTime.toString())
				.queryParam("toDateTime", toDateTime.toString())
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "familyId", familyId)))
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Webmessage.class)
			.returnResult()
			.getResponseBody();
		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst()).isEqualTo(webmessage);
		verify(webmessageService).getWebmessages(municipalityId, instanceType, familyId, fromDateTime, toDateTime);
		verifyNoMoreInteractions(webmessageService);
	}

	@Test
	void getWebmessagesWithoutDateFilter() {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = InstanceType.EXTERNAL;
		final var familyId = "123";
		final var webmessage = Webmessage.create().withId(123).withMessage("message");

		when(webmessageService.getWebmessages(municipalityId, instanceType, familyId, null, null)).thenReturn(List.of(webmessage));
		// Act
		final var result = webTestClient.get()
			.uri(builder -> builder.path(PATH + "/{familyId}")
				.build(Map.of("municipalityId", municipalityId, "instanceType", instanceType, "familyId", familyId)))
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Webmessage.class)
			.returnResult()
			.getResponseBody();
		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst()).isEqualTo(webmessage);
		verify(webmessageService).getWebmessages(municipalityId, instanceType, familyId, null, null);
		verifyNoMoreInteractions(webmessageService);
	}
}
