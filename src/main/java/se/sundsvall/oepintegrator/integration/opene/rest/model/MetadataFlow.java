package se.sundsvall.oepintegrator.integration.opene.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MetadataFlow(
	@JsonProperty("FlowFamilyID") String flowFamilyId,
	@JsonProperty("Name") String displayName) {
}
