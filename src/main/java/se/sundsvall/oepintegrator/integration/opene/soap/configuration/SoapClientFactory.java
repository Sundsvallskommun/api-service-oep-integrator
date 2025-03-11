package se.sundsvall.oepintegrator.integration.opene.soap.configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPEncoder;
import feign.soap.SOAPErrorDecoder;
import jakarta.xml.soap.SOAPConstants;
import java.nio.charset.StandardCharsets;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapClient;
import se.sundsvall.oepintegrator.utility.EncryptionUtility;

@Component
public class SoapClientFactory {

	private static final JAXBContextFactory JAXB_FACTORY = new JAXBContextFactory.Builder()
		.withMarshallerJAXBEncoding(StandardCharsets.UTF_8.toString())
		.build();
	private static final SOAPEncoder.Builder ENCODER_BUILDER = new SOAPEncoder.Builder()
		.withCharsetEncoding(StandardCharsets.UTF_8)
		.withFormattedOutput(false)
		.withJAXBContextFactory(JAXB_FACTORY)
		.withSOAPProtocol(SOAPConstants.SOAP_1_1_PROTOCOL)
		.withWriteXmlDeclaration(true);
	private final EncryptionUtility encryptionUtility;
	private final ApplicationContext applicationContext;

	public SoapClientFactory(final EncryptionUtility encryptionUtility, final ApplicationContext applicationContext) {
		this.encryptionUtility = encryptionUtility;
		this.applicationContext = applicationContext;
	}

	public OpeneClient createSoapClient(final InstanceEntity instanceEntity) {

		final var clientName = "oep-%s-%s".formatted(instanceEntity.getInstanceType().name().toLowerCase(), instanceEntity.getMunicipalityId());

		return new FeignClientBuilder(applicationContext)
			.forType(OpeneSoapClient.class, clientName)
			.customize(builder -> builder
				.encoder(ENCODER_BUILDER.build())
				.errorDecoder(new SOAPErrorDecoder())
				.requestInterceptor(new BasicAuthRequestInterceptor(instanceEntity.getUsername(), encryptionUtility.decrypt(instanceEntity.getPassword())))
				.options(new Request.Options(instanceEntity.getConnectTimeout(), SECONDS, instanceEntity.getReadTimeout(), SECONDS, true)))
			.url(instanceEntity.getBaseUrl())
			.build();

	}
}
