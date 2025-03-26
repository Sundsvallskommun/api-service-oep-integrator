package se.sundsvall.oepintegrator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;

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
}
