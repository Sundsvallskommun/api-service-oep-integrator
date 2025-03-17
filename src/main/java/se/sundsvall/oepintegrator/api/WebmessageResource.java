package se.sundsvall.oepintegrator.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.service.WebmessageService;

@RestController
@Validated
@RequestMapping("/{municipalityId}/{instanceType}/webmessages")
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@Tag(name = "WebMessages", description = "Resource for sending web messages to OpenE")
class WebmessageResource {

	private final WebmessageService webmessageService;

	WebmessageResource(final WebmessageService webmessageService) {
		this.webmessageService = webmessageService;
	}

	@PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = ALL_VALUE)
	@Operation(summary = "Create web message", responses = {
		@ApiResponse(responseCode = "201", headers = @Header(name = LOCATION, schema = @Schema(type = "string")), description = "Successful operation", useReturnTypeSchema = true)
	})
	ResponseEntity<Void> createWebmessage(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "Which instanceType a message should be sent to", example = "1") @PathVariable final InstanceType instanceType,
		@Valid @RequestPart final WebmessageRequest request,
		@RequestPart(value = "attachments", required = false) final List<MultipartFile> attachments) {

		return created(fromPath("/{municipalityId}/webmessages/{id}").buildAndExpand(municipalityId, webmessageService.createWebmessage(municipalityId, instanceType, request, attachments)).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@GetMapping(path = "/{familyId}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get all web messages for a familyId", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = WebmessageRequest.class)))
	})
	ResponseEntity<List<Webmessage>> getWebmessages(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "Which instanceType a message should be sent to", example = "1") @PathVariable final InstanceType instanceType,
		@Parameter(name = "familyId", description = "Family id", example = "123") @PathVariable final String familyId,
		@Parameter(name = "fromDate", description = "The start date for filtering web messages (optional)", example = "2023-02-23 17:26:23") @RequestParam(required = false) final String fromDate,
		@Parameter(name = "toDate", description = "The end date for filtering web messages (optional)", example = "2023-02-23 17:26:23") @RequestParam(required = false) final String toDate) {

		return ResponseEntity.ok(webmessageService.getWebmessages(municipalityId, instanceType, familyId, fromDate, toDate));
	}
}
