package se.sundsvall.oepintegrator.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.oepintegrator.api.model.webmessage.WebMessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.service.WebMessageService;

@RestController
@Validated
@RequestMapping("/{municipalityId}/{instanceType}/webmessages")
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@Tag(name = "WebMessages", description = "Resource for sending web messages to OpenE")
class WebMessageResource {

	private final WebMessageService webMessageService;

	WebMessageResource(final WebMessageService webMessageService) {
		this.webMessageService = webMessageService;
	}

	@PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = ALL_VALUE)
	@Operation(summary = "Create web message", responses = {
		@ApiResponse(responseCode = "201", headers = @Header(name = LOCATION, schema = @Schema(type = "string")), description = "Successful operation", useReturnTypeSchema = true)
	})
	ResponseEntity<Void> createWebMessage(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "Which instanceType a message should be sent to", example = "1") @PathVariable final InstanceType instanceType,
		@Valid @RequestPart final WebMessageRequest request,
		@RequestPart(value = "files", required = false) final List<MultipartFile> files) {

		return created(fromPath("/{municipalityId}/webmessages/{id}").buildAndExpand(municipalityId, webMessageService.createWebMessage(municipalityId, instanceType, request, files)).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}
}
