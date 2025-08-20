package se.sundsvall.oepintegrator.integration.opene.rest.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class MetadataRootTest {

	@Test
	void builder() {
		// Arrange
		final var familyId = "familyId";
		final var name = "name";
		final var list = List.of(new MetadataFlow(familyId, name));
		// Act
		final var result = new MetadataRoot(list);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.metadataFlows()).hasSize(1);
		assertThat(result.metadataFlows().getFirst().displayName()).isEqualTo(name);
		assertThat(result.metadataFlows().getFirst().flowFamilyId()).isEqualTo(familyId);

	}

}
