package se.sundsvall.oepintegrator.integration.opene.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
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
	void getCaseList(@Load("/mappings/flow-instances.xml") final String xml) {

		// Arrange
		final var municipalityId = "2281";
		final var instanceType = EXTERNAL;
		final var familyId = "familyId";
		final var status = "status";
		final var fromDate = LocalDate.now();
		final var toDate = LocalDate.now();

		when(clientFactory.getRestClient(municipalityId, instanceType)).thenReturn(openeRestClient);
		when(openeRestClient.getCaseListByFamilyId(familyId, status, fromDate, toDate)).thenReturn(Optional.of(xml.getBytes()));

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
		verify(openeRestClient).getCaseListByFamilyId(familyId, status, fromDate, toDate);
		verifyNoMoreInteractions(openeRestClient, clientFactory);
	}

	@Test
	void getCaseListWithoutOptionalParameters(@Load("/mappings/flow-instances.xml") final String xml) {

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
}
