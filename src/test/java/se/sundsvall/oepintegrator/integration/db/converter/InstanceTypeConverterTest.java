package se.sundsvall.oepintegrator.integration.db.converter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.INTERNAL;

class InstanceTypeConverterTest {

	private final InstanceTypeConverter converter = new InstanceTypeConverter();

	@Test
	void convertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(null)).isNull();
		assertThat(converter.convertToDatabaseColumn(INTERNAL)).isEqualTo("INTERNAL");
		assertThat(converter.convertToDatabaseColumn(EXTERNAL)).isEqualTo("EXTERNAL");
	}

	@Test
	void convertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute(null)).isNull();
		assertThat(converter.convertToEntityAttribute("INTERNAL")).isEqualTo(INTERNAL);
		assertThat(converter.convertToEntityAttribute("EXTERNAL")).isEqualTo(EXTERNAL);
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
