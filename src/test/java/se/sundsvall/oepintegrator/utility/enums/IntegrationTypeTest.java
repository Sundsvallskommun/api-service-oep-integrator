package se.sundsvall.oepintegrator.utility.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IntegrationTypeTest {

	@Test
	void enumValues() {
		assertThat(IntegrationType.values()).containsExactlyInAnyOrder(
			IntegrationType.SOAP,
			IntegrationType.REST);
	}

	@Test
	void enumToString() {
		assertThat(IntegrationType.SOAP).hasToString("SOAP");
		assertThat(IntegrationType.REST).hasToString("REST");
	}

}
