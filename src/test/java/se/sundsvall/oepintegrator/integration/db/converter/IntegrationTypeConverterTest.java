package se.sundsvall.oepintegrator.integration.db.converter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static se.sundsvall.oepintegrator.util.enums.IntegrationType.REST;
import static se.sundsvall.oepintegrator.util.enums.IntegrationType.SOAP;

class IntegrationTypeConverterTest {

	private final IntegrationTypeConverter converter = new IntegrationTypeConverter();

	@Test
	void convertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(null)).isNull();
		assertThat(converter.convertToDatabaseColumn(REST)).isEqualTo("REST");
		assertThat(converter.convertToDatabaseColumn(SOAP)).isEqualTo("SOAP");
	}

	@Test
	void convertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute(null)).isNull();
		assertThat(converter.convertToEntityAttribute("REST")).isEqualTo(REST);
		assertThat(converter.convertToEntityAttribute("SOAP")).isEqualTo(SOAP);
	}

	@Test
	void convertToEntityAttribute_withInvalidValue() {
		// arrange
		final String invalidValue = "INVALID_TYPE";

		// act & assert
		assertThatThrownBy(() -> converter.convertToEntityAttribute(invalidValue))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("No enum constant");
	}
}
