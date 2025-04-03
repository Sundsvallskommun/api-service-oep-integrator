package se.sundsvall.oepintegrator.api;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
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

	@GetMapping(path = "/parties/{partyId}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get cases by citizen identifier",
		description = "Get a list of case envelopes by citizen identifier",
		responses = @ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true))
	ResponseEntity<List<CaseEnvelope>> getCasesByPartyId(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "partyId", description = "The party ID", example = "123") @PathVariable final String partyId,
		@Parameter(name = "fromDate", description = "Filter cases on fromDate", example = "2024-01-01") @RequestParam(value = "fromDate", required = false) final LocalDate fromDate,
		@Parameter(name = "toDate", description = "Filter cases on toDate", example = "2024-01-31") @RequestParam(value = "toDate", required = false) final LocalDate toDate,
		@Parameter(name = "status", description = "Filter by status", example = "Preliminär") @RequestParam(value = "status", required = false) final String status) {

		return ok(caseService.getCaseEnvelopeListByCitizenIdentifier(municipalityId, instanceType, partyId, status, fromDate, toDate));
	}

	@PutMapping(value = "/{flowInstanceId}/status", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Set status", description = "Sets status of a case", responses = {
		@ApiResponse(responseCode = "204", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<SetStatusResponse> setStatus(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "flow-instance ID", example = "112233") @PathVariable final String flowInstanceId,
		@NotNull @ValidSetStatusRequest @RequestBody final SetStatusRequest setStatusRequest) {

		return ok(caseService.setStatusByFlowinstanceId(municipalityId, instanceType, setStatusRequest, flowInstanceId));
	}

	@PutMapping(value = "/systems/{system}/{externalId}/status", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Set status", description = "Sets status of a case", responses = {
		@ApiResponse(responseCode = "204", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<SetStatusResponse> setStatus(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "system", description = "The system where external ID exists", example = "CaseData") @PathVariable final String system,
		@Parameter(name = "externalId", description = "Case ID in specified system", example = "234") @PathVariable final String externalId,
		@NotNull @ValidSetStatusRequest @RequestBody final SetStatusRequest setStatusRequest) {

		return ok(caseService.setStatusByExternalId(municipalityId, instanceType, setStatusRequest, system, externalId));
	}

	@PostMapping(value = "/{flowInstanceId}/delivery", consumes = APPLICATION_JSON_VALUE, produces = ALL_VALUE)
	@Operation(summary = "Confirm delivery", description = "Confirms delivery of a case", responses = {
		@ApiResponse(responseCode = "204", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<Void> confirmDelivery(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "flow-instance ID", example = "112233") @PathVariable final String flowInstanceId,
		@NotNull @Valid @RequestBody final ConfirmDeliveryRequest confirmDeliveryRequest) {
		caseService.confirmDelivery(municipalityId, instanceType, flowInstanceId, confirmDeliveryRequest);
		return noContent().build();
	}

	@GetMapping(value = "/{flowInstanceId}/pdf", produces = ALL_VALUE)
	@Operation(summary = "Get case PDF", description = "Get case PDF by flow instance ID", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	void getCasePdfByFlowInstanceId(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "flow-instance ID", example = "112233") @PathVariable final String flowInstanceId,
		final HttpServletResponse response) {
		caseService.getCasePdfByFlowInstanceId(municipalityId, instanceType, flowInstanceId, response);
	}

	@GetMapping(path = "/{flowInstanceId}/status", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get case status", description = "Get case status by flow instance ID", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<CaseStatus> getCaseStatusByFlowInstanceId(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "flow-instance id", example = "112233") @PathVariable final String flowInstanceId) {
		return ok(caseService.getCaseStatusByFlowInstanceId(municipalityId, instanceType, flowInstanceId));
	}

	@GetMapping(value = "/{flowInstanceId}/queries/{queryId}/files/{fileId}", produces = ALL_VALUE)
	@Operation(summary = "Get case attachment", description = "Get case attachment by flowInstanceId, queryId and fileId", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	void getCaseAttachment(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "instanceType", description = "The instanceType where case belongs", example = "INTERNAL") @PathVariable final InstanceType instanceType,
		@Parameter(name = "flowInstanceId", description = "flow-instance ID", example = "112233") @PathVariable final String flowInstanceId,
		@Parameter(name = "queryId", description = "Query ID", example = "112233") @PathVariable final String queryId,
		@Parameter(name = "fileId", description = "File ID", example = "112233") @PathVariable final String fileId,
		final HttpServletResponse response) {

		// TODO: Implement
	}
}
