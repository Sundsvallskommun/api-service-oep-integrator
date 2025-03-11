package se.sundsvall.oepintegrator.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.db.model.enums.IntegrationType;

class InstanceTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(Instance.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var id = "123e4567-e89b-12d3-a456-426614174000";
		final var integrationType = IntegrationType.REST;
		final var instance = InstanceType.EXTERNAL;
		final var baseUrl = "https://example.com";
		final var username = "user123";
		final var password = "pass123";
		final var familyIds = List.of("family1", "family2");
		final var connectTimeout = 5;
		final var readTimeout = 60;

		// Act
		final var result = Instance.create()
			.withId(id)
			.withIntegrationType(integrationType)
			.withInstanceType(instance)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withPassword(password)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getIntegrationType()).isEqualTo(integrationType);
		assertThat(result.getInstanceType()).isEqualTo(instance);
		assertThat(result.getBaseUrl()).isEqualTo(baseUrl);
		assertThat(result.getUsername()).isEqualTo(username);
		assertThat(result.getPassword()).isEqualTo(password);
		assertThat(result.getFamilyIds()).isEqualTo(familyIds);
		assertThat(result.getConnectTimeout()).isEqualTo(connectTimeout);
		assertThat(result.getReadTimeout()).isEqualTo(readTimeout);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Instance.create()).hasAllNullFieldsOrProperties();
		assertThat(new Instance()).hasAllNullFieldsOrProperties();
	}

}
