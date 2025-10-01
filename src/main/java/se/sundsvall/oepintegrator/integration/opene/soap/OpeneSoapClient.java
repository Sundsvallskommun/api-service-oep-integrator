package se.sundsvall.oepintegrator.integration.opene.soap;

import callback.AddMessage;
import callback.AddMessageAsOwner;
import callback.AddMessageAsOwnerResponse;
import callback.AddMessageResponse;
import callback.ConfirmDelivery;
import callback.SetStatus;
import callback.SetStatusResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;

/**
 * Interface for OpenE SOAP clients. This contains the methods for calling the Open-E SOAP API.
 */
public interface OpeneSoapClient extends OpeneClient {

	String TEXT_XML_UTF_8 = "text/xml; charset=UTF-8";

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_UTF_8, produces = TEXT_XML_UTF_8)
	void confirmDelivery(@RequestBody ConfirmDelivery confirmDelivery);

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_UTF_8, produces = TEXT_XML_UTF_8)
	AddMessageResponse addMessage(@RequestBody final AddMessage addMessage);

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_UTF_8, produces = TEXT_XML_UTF_8)
	AddMessageAsOwnerResponse addMessageAsOwner(@RequestBody final AddMessageAsOwner addMessageAsOwner);

	@PostMapping(path = "/api/callback", consumes = TEXT_XML_UTF_8, produces = TEXT_XML_UTF_8)
	SetStatusResponse setStatus(@RequestBody SetStatus setStatus);
}
