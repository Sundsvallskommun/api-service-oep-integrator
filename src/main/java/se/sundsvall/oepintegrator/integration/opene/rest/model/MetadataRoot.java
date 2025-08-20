package se.sundsvall.oepintegrator.integration.opene.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record MetadataRoot(@JsonProperty("Flows") List<MetadataFlow> metadataFlows) {
}
