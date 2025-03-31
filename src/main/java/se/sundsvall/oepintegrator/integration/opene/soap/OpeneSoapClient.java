package se.sundsvall.oepintegrator.integration.opene.soap;

import callback.AddMessage;
import callback.AddMessageResponse;
import callback.ConfirmDelivery;
import callback.SetStatus;
import callback.SetStatusResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;

/**
 * Interface for OpenE SOAP clients. This contains the methods for calling the Open-E SOAP API.
 */
@CircuitBreaker(name = "open-e-soap")
public interface OpeneSoapClient extends OpeneClient {

	String TEXT_XML_CHARSET_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	void confirmDelivery(@RequestBody ConfirmDelivery confirmDelivery);

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	AddMessageResponse addMessage(@RequestBody final AddMessage addMessage);

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	SetStatusResponse setStatus(@RequestBody SetStatus setStatus);
}
