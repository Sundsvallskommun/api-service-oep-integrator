package se.sundsvall.oepintegrator.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;
import se.sundsvall.oepintegrator.api.validation.ValidSetStatusRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;

@RestController
@Validated
@RequestMapping("/{municipalityId}/{instanceType}/cases")
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@Tag(name = "Case", description = "Operations on case")
class CaseResource {

	CaseResource() {
		// TODO Add service
	}

	@PutMapping("/flowInstanceId/{flowInstanceId}/status")
	@Operation(summary = "Set status", description = "Sets status of a case", responses = {
		@ApiResponse(responseCode = "204", description = "Successful operation"),
	})
	ResponseEntity<Void> setStatus(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "flow-instance id", example = "112233") @PathVariable final String flowInstanceId,
		@NotNull @ValidSetStatusRequest @RequestBody final SetStatusRequest setStatusRequest) {
		// TODO Add service call
		return noContent()
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@PutMapping("/system/{system}/externalId/{externalId}/status")
	@Operation(summary = "Set status", description = "Sets status of a case", responses = {
		@ApiResponse(responseCode = "204", description = "Successful operation"),
	})
	ResponseEntity<Void> setStatus(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "system", description = "The system where external id exists", example = "CaseData") @PathVariable final String system,
		@Parameter(name = "externalId", description = "Case id in specified system", example = "234") @PathVariable final String externalId,
		@NotNull @ValidSetStatusRequest @RequestBody final SetStatusRequest setStatusRequest) {
		// TODO Add service call
		return noContent()
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}
}
