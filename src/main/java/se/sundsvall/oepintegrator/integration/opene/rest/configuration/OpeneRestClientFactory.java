package se.sundsvall.oepintegrator.integration.opene.rest.configuration;

import static feign.Logger.Level.FULL;
import static java.util.concurrent.TimeUnit.SECONDS;

import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestClient;
import se.sundsvall.oepintegrator.util.EncryptionUtility;

@Component
public class OpeneRestClientFactory {

	private final ApplicationContext applicationContext;

	private final EncryptionUtility encryptionUtility;

	public OpeneRestClientFactory(final ApplicationContext applicationContext, final EncryptionUtility encryptionUtility) {
		this.applicationContext = applicationContext;
		this.encryptionUtility = encryptionUtility;
	}

	public OpeneClient createRestClient(final InstanceEntity instanceEntity) {

		final var clientName = "oep-%s-%s".formatted(instanceEntity.getInstanceType().name().toLowerCase(), instanceEntity.getMunicipalityId());

		return new FeignClientBuilder(applicationContext)
			.forType(OpeneRestClient.class, clientName)
			.customize(builder -> builder
				.errorDecoder(new ProblemErrorDecoder(clientName))
				.requestInterceptor(new BasicAuthRequestInterceptor(instanceEntity.getUsername(), encryptionUtility.decrypt(instanceEntity.getPassword())))
				.logLevel(FULL)
				.dismiss404()
				.options(new Request.Options(instanceEntity.getConnectTimeout(), SECONDS, instanceEntity.getReadTimeout(), SECONDS, true)))
			.url(instanceEntity.getBaseUrl())
			.build();
	}
}
