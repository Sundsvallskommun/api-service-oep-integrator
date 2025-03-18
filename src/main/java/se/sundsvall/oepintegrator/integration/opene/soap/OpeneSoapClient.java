package se.sundsvall.oepintegrator.integration.opene.soap;

import callback.AddMessage;
import callback.AddMessageResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;

/**
 * Interface for OpenE SOAP clients. This contains the methods for calling the Open-E SOAP API.
 */
@CircuitBreaker(name = "open-e-soap")
public interface OpeneSoapClient extends OpeneClient {

	String TEXT_XML_CHARSET_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	AddMessageResponse addMessage(@RequestBody AddMessage addMessage);

	@GetMapping(path = "/api/messageapi/getmessages/family/{familyId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getMessages(@PathVariable(name = "familyId") final String familyId,
		@RequestParam(name = "fromDate") final String fromDate,
		@RequestParam(name = "toDate") final String toDate);
}
