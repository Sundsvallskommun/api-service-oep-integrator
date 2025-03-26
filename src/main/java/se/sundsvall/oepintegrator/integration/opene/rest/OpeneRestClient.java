package se.sundsvall.oepintegrator.integration.opene.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;

/**
 * Interface for OpenE REST clients. This contains the methods for calling the Open-E REST API.
 */
@CircuitBreaker(name = "open-e-rest")
public interface OpeneRestClient extends OpeneClient {

	String TEXT_XML_CHARSET_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

	@GetMapping(path = "/api/instanceapi/getinstances/family/{familyId}/{status}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	Optional<byte[]> getCaseListByFamilyId(
		@PathVariable(name = "familyId") final String familyId,
		@PathVariable(name = "status", required = false) final String status,
		@RequestParam(name = "fromDate", required = false) final LocalDate fromDate,
		@RequestParam(name = "toDate", required = false) final LocalDate toDate);
}
