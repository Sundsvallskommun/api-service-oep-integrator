package se.sundsvall.oepintegrator.integration.opene.soap.configuration;

import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import feign.soap.SOAPErrorDecoder;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.opene.CircuitBreakerCapability;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapClient;
import se.sundsvall.oepintegrator.util.EncryptionUtility;

import static feign.Logger.Level.FULL;
import static jakarta.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class SoapClientFactory {

	private static final JAXBContextFactory JAXB_FACTORY = new JAXBContextFactory.Builder()
		.withMarshallerJAXBEncoding(UTF_8.toString())
		.build();
	private static final SOAPEncoder.Builder ENCODER_BUILDER = new SOAPEncoder.Builder()
		.withCharsetEncoding(UTF_8)
		.withFormattedOutput(false)
		.withJAXBContextFactory(JAXB_FACTORY)
		.withSOAPProtocol(SOAP_1_1_PROTOCOL)
		.withWriteXmlDeclaration(true);
	private static final SOAPDecoder.Builder DECODER_BUILDER = new SOAPDecoder.Builder()
		.withJAXBContextFactory(JAXB_FACTORY);
	private final EncryptionUtility encryptionUtility;
	private final ApplicationContext applicationContext;
	private final CircuitBreakerRegistry circuitBreakerRegistry;

	public SoapClientFactory(final EncryptionUtility encryptionUtility, final ApplicationContext applicationContext, final CircuitBreakerRegistry circuitBreakerRegistry) {
		this.encryptionUtility = encryptionUtility;
		this.applicationContext = applicationContext;
		this.circuitBreakerRegistry = circuitBreakerRegistry;
	}

	public OpeneClient createSoapClient(final InstanceEntity instanceEntity) {

		final var clientName = "oep-%s-%s-%s".formatted(instanceEntity.getInstanceType().name().toLowerCase(), instanceEntity.getMunicipalityId(), instanceEntity.getIntegrationType().name().toLowerCase());
		final var circuitBreaker = circuitBreakerRegistry.circuitBreaker(clientName);

		return new FeignClientBuilder(applicationContext)
			.forType(OpeneSoapClient.class, clientName)
			.customize(builder -> builder
				.encoder(ENCODER_BUILDER.build())
				.decoder(DECODER_BUILDER.build())
				.errorDecoder(new SOAPErrorDecoder())
				.logLevel(FULL)
				.addCapability(new CircuitBreakerCapability(circuitBreaker))
				.requestInterceptor(new BasicAuthRequestInterceptor(instanceEntity.getUsername(), encryptionUtility.decrypt(instanceEntity.getPassword())))
				.options(new Request.Options(instanceEntity.getConnectTimeout(), SECONDS, instanceEntity.getReadTimeout(), SECONDS, true)))
			.url(instanceEntity.getBaseUrl())
			.build();
	}
}
