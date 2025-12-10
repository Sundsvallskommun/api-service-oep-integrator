package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Direction model", examples = "INBOUND", enumAsRef = true)
public enum Direction {
	INBOUND,
	OUTBOUND
}
