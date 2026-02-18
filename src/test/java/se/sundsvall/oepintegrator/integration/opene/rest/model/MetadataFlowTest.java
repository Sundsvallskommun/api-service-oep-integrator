package se.sundsvall.oepintegrator.integration.opene.rest.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MetadataFlowTest {

	@Test
	void builder() {
		// Arrange
		final var familyId = "familyId";
		final var name = "name";

		// Act
		final var result = new MetadataFlow(familyId, name);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.flowFamilyId()).isEqualTo(familyId);
		assertThat(result.displayName()).isEqualTo(name);
	}

}
