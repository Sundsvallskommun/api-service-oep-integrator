package se.sundsvall.oepintegrator.integration.opene.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;

/**
 * Interface for OpenE REST clients. This contains the methods for calling the Open-E REST API.
 */
@CircuitBreaker(name = "open-e-rest")
public interface OpeneRestClient extends OpeneClient {

	String TEXT_XML_CHARSET_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

}
