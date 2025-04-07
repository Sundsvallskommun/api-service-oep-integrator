package se.sundsvall.oepintegrator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;

@ExtendWith(ResourceLoaderExtension.class)
class CaseMapperTest {

	@Test
	void toCaseEnvelopList(@Load("/mappings/flow-instances.xml") final String xml) {

		// Act
		final var result = CaseMapper.toCaseEnvelopeList(xml.getBytes());

		// Assert
		assertThat(result).isNotNull().hasSize(4);
		assertThat(result)
			.extracting(CaseEnvelope::getFlowInstanceId, CaseEnvelope::getCreated, CaseEnvelope::getStatusUpdated)
			.containsExactlyInAnyOrder(
				tuple("4999", LocalDateTime.parse("2025-02-27T11:16"), LocalDateTime.parse("2025-03-06T09:10")),
				tuple("4965", LocalDateTime.parse("2025-02-18T19:12"), LocalDateTime.parse("2025-02-18T19:45")),
				tuple("4933", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-14T12:40")),
				tuple("4932", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-18T20:10")));
	}

	@Test
	void toCaseEnvelopListWhenResultIsEmpty(@Load("/mappings/empty-flow-instances.xml") final String xml) {

		// Act
		final var result = CaseMapper.toCaseEnvelopeList(xml.getBytes());

		// Assert
		assertThat(result).isEmpty();
	}

	@Test
	void toConfirmDelivery() {
		// Arrange
		final var flowInstanceId = "123456";
		final var request = new ConfirmDeliveryRequest()
			.withCaseId("789012")
			.withDelivered(true)
			.withLogMessage("The case was delivered successfully")
			.withSystem("ByggR");

		// Act
		final var result = CaseMapper.toConfirmDelivery(flowInstanceId, request);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(123456);
		assertThat(result.getExternalID().getID()).isEqualTo("789012");
		assertThat(result.getExternalID().getSystem()).isEqualTo("ByggR");
		assertThat(result.isDelivered()).isTrue();
		assertThat(result.getLogMessage()).isEqualTo("The case was delivered successfully");
	}

	@Test
	void toConfirmDeliveryWithNullValues() {
		// Arrange
		final var flowInstanceId = "123456";
		final var request = new ConfirmDeliveryRequest()
			.withCaseId(null)
			.withDelivered(false)
			.withLogMessage(null)
			.withSystem(null);

		// Act
		final var result = CaseMapper.toConfirmDelivery(flowInstanceId, request);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(Integer.valueOf(flowInstanceId));
		assertThat(result.getExternalID().getID()).isNull();
		assertThat(result.getExternalID().getSystem()).isNull();
		assertThat(result.isDelivered()).isFalse();
		assertThat(result.getLogMessage()).isNull();
	}
}
