package se.sundsvall.oepintegrator.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageAttachmentData;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.service.WebmessageService;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

@RestController
@Validated
@RequestMapping("/{municipalityId}/{instanceType}/webmessages")
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "502", description = "Bad gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@Tag(name = "Webmessages", description = "Resource for sending webmessages to OpenE")
class WebmessageResource {

	private final WebmessageService webmessageService;

	WebmessageResource(final WebmessageService webmessageService) {
		this.webmessageService = webmessageService;
	}

	@PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = ALL_VALUE)
	@Operation(summary = "Create webmessage", responses = {
		@ApiResponse(responseCode = "201", headers = @Header(name = LOCATION, schema = @Schema(type = "string")), description = "Successful operation", useReturnTypeSchema = true)
	})
	ResponseEntity<Void> createWebmessage(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "Which instanceType a message should be sent to", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Valid @RequestPart final WebmessageRequest request,
		@RequestPart(value = "attachments", required = false) final List<MultipartFile> attachments) {

		return created(fromPath("/{municipalityId}/webmessages/{id}").buildAndExpand(municipalityId, webmessageService.createWebmessage(municipalityId, instanceType, request, attachments)).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@GetMapping(path = "/families/{familyId}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get all webmessages for a familyId", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = APPLICATION_JSON_VALUE), useReturnTypeSchema = true)
	})
	ResponseEntity<List<Webmessage>> getWebmessagesByFamilyId(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "Which instanceType a message should be sent to", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "familyId", description = "Family id", example = "123") @PathVariable final String familyId,
		@Parameter(name = "fromDateTime", description = "The start date and time for filtering web messages (optional)", example = "2024-01-31T12:00:00") @RequestParam(required = false) @DateTimeFormat(
			iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime fromDateTime,
		@Parameter(name = "toDateTime", description = "The end date and time for filtering web messages (optional).", example = "2024-01-31T12:00:00") @RequestParam(required = false) @DateTimeFormat(
			iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime toDateTime) {
		return ok(webmessageService.getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDateTime, toDateTime));
	}

	@GetMapping(path = "/flow-instances/{flowInstanceId}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get all webmessages for a given errand", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = APPLICATION_JSON_VALUE), useReturnTypeSchema = true)
	})
	ResponseEntity<List<Webmessage>> getWebmessagesByFlowInstanceId(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "Which instanceType a message should be sent to", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "Flow instance id", example = "123") @PathVariable final String flowInstanceId,
		@Parameter(name = "fromDateTime", description = "The start date and time for filtering web messages (optional)", example = "2024-01-31T12:00:00") @RequestParam(required = false) @DateTimeFormat(
			iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime fromDateTime,
		@Parameter(name = "toDateTime", description = "The end date and time for filtering web messages (optional).", example = "2024-01-31T12:00:00") @RequestParam(required = false) @DateTimeFormat(
			iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime toDateTime) {
		return ok(webmessageService.getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDateTime, toDateTime));
	}

	@GetMapping(path = "/flow-instances/{flowInstanceId}/attachments/{attachmentId}")
	@Operation(summary = "Get attachment by id", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = APPLICATION_JSON_VALUE), useReturnTypeSchema = true)
	})
	ResponseEntity<WebmessageAttachmentData> getAttachmentById(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "Which instanceType a message should be sent to", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "Flow instance id", example = "123") @PathVariable final String flowInstanceId,
		@Parameter(name = "attachmentId", description = "Attachment id", example = "123") @PathVariable @NotNull final Integer attachmentId) {
		return ok(webmessageService.getAttachmentById(municipalityId, instanceType, attachmentId));
	}
}
