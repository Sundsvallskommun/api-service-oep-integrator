package se.sundsvall.oepintegrator.integration.opene.soap;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;

/**
 * Interface for OpenE SOAP clients. This contains the methods for calling the Open-E SOAP API.
 */
@CircuitBreaker(name = "open-e-soap")
public interface OpeneSoapClient extends OpeneClient {

	String TEXT_XML_CHARSET_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

}
