package se.sundsvall.oepintegrator.integration.opene;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType.INTERNAL;
import static se.sundsvall.oepintegrator.integration.db.model.enums.IntegrationType.REST;
import static se.sundsvall.oepintegrator.integration.db.model.enums.IntegrationType.SOAP;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.utility.EncryptionUtility;

@SpringBootTest(classes = Application.class, webEnvironment = MOCK)
@ActiveProfiles("junit")
class OpeneClientFactoryTest {

	@Autowired
	private ApplicationContext context;

	@MockitoBean
	private EncryptionUtility encryptionUtilityMock;

	@Autowired
	private OpeneClientFactory openEClientFactory;

	@Test
	void testCreateAndGetClient() {
		final var instanceEntity = InstanceEntity.create()
			.withInstanceType(EXTERNAL)
			.withIntegrationType(SOAP)
			.withMunicipalityId("municipalityId")
			.withId("instanceId")
			.withUsername("username")
			.withPassword("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")
			.withBaseUrl("http://localhost")
			.withConnectTimeout(10)
			.withReadTimeout(10);

		when(encryptionUtilityMock.decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")).thenReturn("decryptedPassword");

		openEClientFactory.createClient(instanceEntity);

		final var client = openEClientFactory.getClient("municipalityId", EXTERNAL);
		assertThat(client).isNotNull();
		verify(encryptionUtilityMock).decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==");

	}

	@Test
	void testCreateAndGetRestClient() {
		final var instanceEntity = InstanceEntity.create()
			.withInstanceType(EXTERNAL)
			.withIntegrationType(REST)
			.withMunicipalityId("municipalityId")
			.withId("instanceId")
			.withUsername("username")
			.withPassword("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")
			.withBaseUrl("http://localhost")
			.withConnectTimeout(10)
			.withReadTimeout(10);

		when(encryptionUtilityMock.decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")).thenReturn("decryptedPassword");

		openEClientFactory.createClient(instanceEntity);

		final var client = openEClientFactory.getRestClient("municipalityId", EXTERNAL);
		assertThat(client).isNotNull();
		verify(encryptionUtilityMock).decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==");

	}

	@Test
	void testCreateAndGetClientNotFound() {
		assertThatThrownBy(() -> openEClientFactory.getClient("municipalityId", EXTERNAL))
			.hasMessage("Internal Server Error: No EXTERNAL OpenE client exists for municipalityId municipalityId")
			.isInstanceOf(Problem.class);
	}

	@Test
	void testCreateAndGetRestClientNotFound() {
		assertThatThrownBy(() -> openEClientFactory.getRestClient("municipalityId", EXTERNAL))
			.hasMessage("Internal Server Error: No EXTERNAL OpenE REST client exists for municipalityId municipalityId")
			.isInstanceOf(Problem.class);
	}

	@Test
	void testCreateAndGetSoapClientNotFound() {
		assertThatThrownBy(() -> openEClientFactory.getSoapClient("municipalityId", INTERNAL))
			.hasMessage("Internal Server Error: No INTERNAL OpenE SOAP client exists for municipalityId municipalityId")
			.isInstanceOf(Problem.class);
	}

	@Test
	void testCreateAndGetSoapClient() {
		final var instanceEntity = InstanceEntity.create()
			.withInstanceType(EXTERNAL)
			.withIntegrationType(SOAP)
			.withMunicipalityId("municipalityId")
			.withId("instanceId")
			.withUsername("username")
			.withPassword("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")
			.withBaseUrl("http://localhost")
			.withConnectTimeout(10)
			.withReadTimeout(10);

		when(encryptionUtilityMock.decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")).thenReturn("decryptedPassword");

		openEClientFactory.createClient(instanceEntity);

		final var client = openEClientFactory.getSoapClient("municipalityId", EXTERNAL);
		assertThat(client).isNotNull();
		verify(encryptionUtilityMock).decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==");

	}

	@Test
	void testCreateAndRemoveClient() {
		final var instanceEntity = InstanceEntity.create()
			.withInstanceType(INTERNAL)
			.withIntegrationType(REST)
			.withMunicipalityId("municipalityId")
			.withId("instanceId")
			.withUsername("username")
			.withPassword("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")
			.withBaseUrl("http://localhost")
			.withConnectTimeout(10)
			.withReadTimeout(10);

		when(encryptionUtilityMock.decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==")).thenReturn("decryptedPassword");

		openEClientFactory.createClient(instanceEntity);
		openEClientFactory.removeClient("municipalityId", INTERNAL);

		verify(encryptionUtilityMock).decrypt("zOHnQlCA1VbKpcHLnK+MVAiCwdp0KOPIR2MkhHwGY/ft9ltPdw==");
	}
}
