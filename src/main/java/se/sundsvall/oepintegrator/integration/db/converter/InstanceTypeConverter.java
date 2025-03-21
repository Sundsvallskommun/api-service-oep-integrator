package se.sundsvall.oepintegrator.integration.db.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

@Converter(autoApply = true)
public class InstanceTypeConverter implements AttributeConverter<InstanceType, String> {

	@Override
	public String convertToDatabaseColumn(final InstanceType attribute) {
		return Optional.ofNullable(attribute).map(InstanceType::toString).orElse(null);
	}

	@Override
	public InstanceType convertToEntityAttribute(final String dbData) {
		if (dbData == null) {
			return null;
		}
		return InstanceType.valueOf(dbData);
	}
}
