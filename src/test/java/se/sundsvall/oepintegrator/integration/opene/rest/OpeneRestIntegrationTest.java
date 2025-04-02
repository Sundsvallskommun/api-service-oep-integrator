package se.sundsvall.oepintegrator.integration.opene.rest;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.Problem;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.webmessage.Direction;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;

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

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var attachmentId = 123;

		final var headers = Map.of(
			"Content-Type", List.of("application/pdf"),
			"Content-Disposition", List.of("attachment; filename=case.pdf"),
			"Content-Length", List.of("0"),
			"Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[10]));
		final var responseEntity = ResponseEntity.ok()
			.headers(httpHeaders -> httpHeaders.putAll(headers))
			.body(inputStreamResource);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getAttachmentById(attachmentId)).thenReturn(responseEntity);

		// Act
		final var result = openeRestIntegration.getAttachmentById(municipalityId, instanceType, attachmentId);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getHeaders()).containsEntry("Content-Type", List.of("application/pdf"));
		assertThat(result.getHeaders()).containsEntry("Content-Disposition", List.of("attachment; filename=case.pdf"));
		assertThat(result.getHeaders()).containsEntry("Content-Length", List.of("0"));
		assertThat(result.getHeaders()).containsEntry("Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		verify(openeRestClient).getAttachmentById(attachmentId);
		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getAttachmentByIdThrowsException() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var attachmentId = 123;

		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[0]));
		final var responseEntity = ResponseEntity.internalServerError().body(inputStreamResource);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getAttachmentById(attachmentId)).thenReturn(responseEntity);

		// Act & Assert
		assertThatThrownBy(() -> openeRestIntegration.getAttachmentById(municipalityId, instanceType, attachmentId))
			.isInstanceOf(Problem.class)
			.hasMessageStartingWith("Internal Server Error: Failed to get attachment by ID");

		verify(openeRestClient).getAttachmentById(attachmentId);
		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCaseListByFamilyId(@Load("/mappings/flow-instances.xml") final String xml) {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var status = "status";
		final var fromDate = LocalDate.now();
		final var toDate = LocalDate.now();

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCaseListByFamilyId(familyId, status, fromDate.format(ISO_LOCAL_DATE), toDate.format(ISO_LOCAL_DATE))).thenReturn(Optional.of(xml.getBytes()));

		// Act
		final var result = openeRestIntegration.getCaseListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate);

		// Assert
		assertThat(result).isNotNull().hasSize(4);
		assertThat(result)
			.extracting(CaseEnvelope::getFlowInstanceId, CaseEnvelope::getCreated, CaseEnvelope::getStatusUpdated)
			.containsExactlyInAnyOrder(
				tuple("4999", LocalDateTime.parse("2025-02-27T11:16"), LocalDateTime.parse("2025-03-06T09:10")),
				tuple("4965", LocalDateTime.parse("2025-02-18T19:12"), LocalDateTime.parse("2025-02-18T19:45")),
				tuple("4933", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-14T12:40")),
				tuple("4932", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-18T20:10")));

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getCaseListByFamilyId(familyId, status, fromDate.format(ISO_LOCAL_DATE), toDate.format(ISO_LOCAL_DATE));
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCaseListByFamilyIdWithoutOptionalParameters(@Load("/mappings/flow-instances.xml") final String xml) {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCaseListByFamilyId(familyId, null, null, null)).thenReturn(Optional.of(xml.getBytes()));

		// Act
		final var result = openeRestIntegration.getCaseListByFamilyId(municipalityId, instanceType, familyId, null, null, null);

		// Assert
		assertThat(result).isNotNull().hasSize(4);
		assertThat(result)
			.extracting(CaseEnvelope::getFlowInstanceId, CaseEnvelope::getCreated, CaseEnvelope::getStatusUpdated)
			.containsExactlyInAnyOrder(
				tuple("4999", LocalDateTime.parse("2025-02-27T11:16"), LocalDateTime.parse("2025-03-06T09:10")),
				tuple("4965", LocalDateTime.parse("2025-02-18T19:12"), LocalDateTime.parse("2025-02-18T19:45")),
				tuple("4933", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-14T12:40")),
				tuple("4932", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-18T20:10")));

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getCaseListByFamilyId(familyId, null, null, null);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCaseListByCitizenIdentifier(@Load("/mappings/flow-instances.xml") final String xml) {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var legalId = "legalId";
		final var status = "status";
		final var fromDate = LocalDate.now();
		final var toDate = LocalDate.now();

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCaseListByCitizenIdentifier(legalId, status, fromDate.format(ISO_LOCAL_DATE), toDate.format(ISO_LOCAL_DATE))).thenReturn(Optional.of(xml.getBytes()));

		// Act
		final var result = openeRestIntegration.getCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, status, fromDate, toDate);

		// Assert
		assertThat(result).isNotNull().hasSize(4);
		assertThat(result)
			.extracting(CaseEnvelope::getFlowInstanceId, CaseEnvelope::getCreated, CaseEnvelope::getStatusUpdated)
			.containsExactlyInAnyOrder(
				tuple("4999", LocalDateTime.parse("2025-02-27T11:16"), LocalDateTime.parse("2025-03-06T09:10")),
				tuple("4965", LocalDateTime.parse("2025-02-18T19:12"), LocalDateTime.parse("2025-02-18T19:45")),
				tuple("4933", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-14T12:40")),
				tuple("4932", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-18T20:10")));

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getCaseListByCitizenIdentifier(legalId, status, fromDate.format(ISO_LOCAL_DATE), toDate.format(ISO_LOCAL_DATE));
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCaseListByCitizenIdentifierWithoutOptionalParameters(@Load("/mappings/flow-instances.xml") final String xml) {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var legalId = "legalId";

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCaseListByCitizenIdentifier(legalId, null, null, null)).thenReturn(Optional.of(xml.getBytes()));

		// Act
		final var result = openeRestIntegration.getCaseListByCitizenIdentifier(municipalityId, instanceType, legalId, null, null, null);

		// Assert
		assertThat(result).isNotNull().hasSize(4);
		assertThat(result)
			.extracting(CaseEnvelope::getFlowInstanceId, CaseEnvelope::getCreated, CaseEnvelope::getStatusUpdated)
			.containsExactlyInAnyOrder(
				tuple("4999", LocalDateTime.parse("2025-02-27T11:16"), LocalDateTime.parse("2025-03-06T09:10")),
				tuple("4965", LocalDateTime.parse("2025-02-18T19:12"), LocalDateTime.parse("2025-02-18T19:45")),
				tuple("4933", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-14T12:40")),
				tuple("4932", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-18T20:10")));

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getCaseListByCitizenIdentifier(legalId, null, null, null);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCasePdfByFlowInstanceId() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";

		final var headers = Map.of(
			"Content-Type", List.of("application/pdf"),
			"Content-Disposition", List.of("attachment; filename=case.pdf"),
			"Content-Length", List.of("0"),
			"Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[0]));
		final var responseEntity = ResponseEntity.ok()
			.headers(httpHeaders -> httpHeaders.putAll(headers))
			.body(inputStreamResource);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCasePdfByFlowInstanceId(familyId)).thenReturn(responseEntity);

		// Act
		final var result = openeRestIntegration.getCasePdfByFlowInstanceId(municipalityId, instanceType, familyId);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getHeaders()).containsEntry("Content-Type", List.of("application/pdf"));
		assertThat(result.getHeaders()).containsEntry("Content-Disposition", List.of("attachment; filename=case.pdf"));
		assertThat(result.getHeaders()).containsEntry("Content-Length", List.of("0"));
		assertThat(result.getHeaders()).containsEntry("Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		verify(openeRestClient).getCasePdfByFlowInstanceId(familyId);
		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCasePdfByFlowInstanceIdError() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";

		final var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(new byte[0]));

		final var responseEntity = ResponseEntity.internalServerError().body(inputStreamResource);

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCasePdfByFlowInstanceId(familyId)).thenReturn(responseEntity);

		// Act & Assert
		assertThatThrownBy(() -> openeRestIntegration.getCasePdfByFlowInstanceId(municipalityId, instanceType, familyId))
			.isInstanceOf(Problem.class)
			.hasMessageStartingWith("Internal Server Error: Failed to get case PDF by flow instance ID");

		verify(openeRestClient).getCasePdfByFlowInstanceId(familyId);
		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCaseStatusByFlowInstanceId(@Load("/mappings/case-status.xml") final String xml) {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "flowInstanceId";

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCaseStatusByFlowInstanceId(flowInstanceId)).thenReturn(Optional.of(xml.getBytes()));

		// Act
		final var result = openeRestIntegration.getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("Inskickat");

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getCaseStatusByFlowInstanceId(flowInstanceId);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCaseStatusByFlowInstanceIdNotFound() {
		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var flowInstanceId = "flowInstanceId";

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCaseStatusByFlowInstanceId(flowInstanceId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> openeRestIntegration.getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId))
			.isInstanceOf(Problem.class)
			.hasMessageStartingWith("Not Found: No status found for flow instance ID: flowInstanceId");

		verify(clientFactory).getRestClient(municipalityId, instanceType);
		verify(openeRestClient).getCaseStatusByFlowInstanceId(flowInstanceId);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}
}
