package se.sundsvall.oepintegrator.integration.db.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;
import se.sundsvall.oepintegrator.integration.db.model.enums.IntegrationType;

@Converter(autoApply = true)
public class IntegrationTypeConverter implements AttributeConverter<IntegrationType, String> {

	@Override
	public String convertToDatabaseColumn(final IntegrationType attribute) {
		return Optional.ofNullable(attribute).map(IntegrationType::toString).orElse(null);
	}

	@Override
	public IntegrationType convertToEntityAttribute(final String dbData) {
		if (dbData == null) {
			return null;
		}
		return IntegrationType.valueOf(dbData);
	}
}
