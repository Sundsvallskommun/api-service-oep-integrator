package se.sundsvall.oepintegrator.util.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "InstanceType type. INTERNAL/EXTERNAL", example = "INTERNAL", enumAsRef = true)
public enum InstanceType {
	INTERNAL,
	EXTERNAL
}
