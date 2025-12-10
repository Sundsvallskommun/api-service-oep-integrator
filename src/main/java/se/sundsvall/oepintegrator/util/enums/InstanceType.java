package se.sundsvall.oepintegrator.util.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "InstanceType type. INTERNAL/EXTERNAL", examples = "INTERNAL", enumAsRef = true)
public enum InstanceType {
	INTERNAL,
	EXTERNAL
}
