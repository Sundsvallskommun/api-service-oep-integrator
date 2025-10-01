package se.sundsvall.oepintegrator.integration.opene.rest.configuration;

import static feign.Logger.Level.FULL;
import static java.util.concurrent.TimeUnit.SECONDS;

import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.opene.CircuitBreakerCapability;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestClient;
import se.sundsvall.oepintegrator.util.EncryptionUtility;

@Component
public class OpeneRestClientFactory {

	private final ApplicationContext applicationContext;

	private final EncryptionUtility encryptionUtility;
	private final CircuitBreakerRegistry circuitBreakerRegistry;

	public OpeneRestClientFactory(final ApplicationContext applicationContext, final EncryptionUtility encryptionUtility, final CircuitBreakerRegistry circuitBreakerRegistry) {
		this.applicationContext = applicationContext;
		this.encryptionUtility = encryptionUtility;
		this.circuitBreakerRegistry = circuitBreakerRegistry;
	}

	public OpeneClient createRestClient(final InstanceEntity instanceEntity) {

		final var clientName = "oep-%s-%s-%s".formatted(instanceEntity.getInstanceType().name().toLowerCase(), instanceEntity.getMunicipalityId(), instanceEntity.getIntegrationType().name().toLowerCase());
		final var circuitBreaker = circuitBreakerRegistry.circuitBreaker(clientName);

		return new FeignClientBuilder(applicationContext)
			.forType(OpeneRestClient.class, clientName)
			.customize(builder -> builder
				.errorDecoder(new ProblemErrorDecoder(clientName))
				.requestInterceptor(new BasicAuthRequestInterceptor(instanceEntity.getUsername(), encryptionUtility.decrypt(instanceEntity.getPassword())))
				.logLevel(FULL)
				.dismiss404()
				.addCapability(new CircuitBreakerCapability(circuitBreaker))
				.options(new Request.Options(instanceEntity.getConnectTimeout(), SECONDS, instanceEntity.getReadTimeout(), SECONDS, true)))
			.url(instanceEntity.getBaseUrl())
			.build();
	}
}
