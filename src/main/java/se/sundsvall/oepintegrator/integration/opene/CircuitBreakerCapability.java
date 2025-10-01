package se.sundsvall.oepintegrator.integration.opene;

import feign.Capability;
import feign.Client;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import java.io.IOException;

public class CircuitBreakerCapability implements Capability {

	private final CircuitBreaker circuitBreaker;

	public CircuitBreakerCapability(CircuitBreaker circuitBreaker) {
		this.circuitBreaker = circuitBreaker;
	}

	@Override
	public Client enrich(Client client) {
		return (request, options) -> circuitBreaker.executeSupplier(() -> {
			try {
				return client.execute(request, options);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
