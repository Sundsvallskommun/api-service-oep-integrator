package se.sundsvall.oepintegrator.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusResponse;
import se.sundsvall.oepintegrator.api.validation.ValidSetStatusRequest;
import se.sundsvall.oepintegrator.service.CaseService;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

@RestController
@Validated
@RequestMapping("/{municipalityId}/{instanceType}/cases")
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "502", description = "Bad gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@Tag(name = "Case", description = "Operations on case")
class CaseResource {

	private final CaseService caseService;

	CaseResource(final CaseService caseService) {
		this.caseService = caseService;
	}

	@GetMapping(path = "/families/{familyId}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get cases by family ID",
		description = "Get a list of case envelopes by family ID",
		responses = @ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true))
	ResponseEntity<List<CaseEnvelope>> getCasesByFamilyId(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "familyId", description = "The family ID", example = "123") @PathVariable final String familyId,
		@Parameter(name = "fromDate", description = "Filter cases on fromDate", example = "2024-01-01") @RequestParam(value = "fromDate", required = false) final LocalDate fromDate,
		@Parameter(name = "toDate", description = "Filter cases on toDate", example = "2024-01-31") @RequestParam(value = "toDate", required = false) final LocalDate toDate,
		@Parameter(name = "status", description = "Filter by status", example = "Preliminär") @RequestParam(value = "status", required = false) final String status) {

		return ok(caseService.getCaseEnvelopeListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate));
	}

	@PutMapping(value = "/{flowInstanceId}/status", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Set status", description = "Sets status of a case", responses = {
		@ApiResponse(responseCode = "204", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<SetStatusResponse> setStatus(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "flow-instance id", example = "112233") @PathVariable final String flowInstanceId,
		@NotNull @ValidSetStatusRequest @RequestBody final SetStatusRequest setStatusRequest) {

		return ok(caseService.setStatusByFlowinstanceId(municipalityId, instanceType, setStatusRequest, flowInstanceId));
	}

	@PutMapping(value = "/systems/{system}/{externalId}/status", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Set status", description = "Sets status of a case", responses = {
		@ApiResponse(responseCode = "204", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<SetStatusResponse> setStatus(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "system", description = "The system where external id exists", example = "CaseData") @PathVariable final String system,
		@Parameter(name = "externalId", description = "Case id in specified system", example = "234") @PathVariable final String externalId,
		@NotNull @ValidSetStatusRequest @RequestBody final SetStatusRequest setStatusRequest) {

		return ok(caseService.setStatusByExternalId(municipalityId, instanceType, setStatusRequest, system, externalId));
	}
}
