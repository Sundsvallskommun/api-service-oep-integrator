package se.sundsvall.oepintegrator.integration.opene.soap;

import callback.AddMessage;
import callback.AddMessageResponse;
import callback.SetStatus;
import callback.SetStatusResponse;
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
	AddMessageResponse addMessage(@RequestBody final AddMessage addMessage);

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	SetStatusResponse setStatus(@RequestBody SetStatus setStatus);

	@GetMapping(path = "/api/messageapi/getmessages/family/{familyId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getWebmessagesByFamilyId(
		@PathVariable(name = "familyId") final String familyId,
		@RequestParam(name = "fromDate") final String fromDate,
		@RequestParam(name = "toDate") final String toDate);

	@GetMapping(path = "/api/messageapi/getmessages/flowinstance/{flowInstanceId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getWebmessagesByFlowInstanceId(
		@PathVariable(name = "flowInstanceId") final String flowInstanceId,
		@RequestParam(name = "fromDate") final String fromDate,
		@RequestParam(name = "toDate") final String toDate);

	@GetMapping(path = "/api/messageapi/getattachment/{attachmentId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getAttachmentById(@PathVariable(name = "attachmentId") final int attachmentId);

}
