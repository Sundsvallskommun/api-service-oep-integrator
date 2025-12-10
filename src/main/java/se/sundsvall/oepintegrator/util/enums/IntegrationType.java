package se.sundsvall.oepintegrator.util.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "IntegrationType model", examples = "SOAP", enumAsRef = true)
public enum IntegrationType {
	SOAP,
	REST
}
