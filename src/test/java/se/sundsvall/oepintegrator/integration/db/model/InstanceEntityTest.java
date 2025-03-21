package se.sundsvall.oepintegrator.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;
import static se.sundsvall.oepintegrator.utility.enums.IntegrationType.REST;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class InstanceEntityTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(InstanceEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var id = "id";
		final var municipalityId = "municipalityId";
		final var integrationType = REST;
		final var instanceType = EXTERNAL;
		final var baseUrl = "baseUrl";
		final var username = "username";
		final var password = "password";
		final var familyIds = List.of("family1", "family2");
		final var connectTimeout = 60;
		final var readTimeout = 5;

		// Act
		final var result = InstanceEntity.create()
			.withId(id)
			.withMunicipalityId(municipalityId)
			.withIntegrationType(integrationType)
			.withInstanceType(instanceType)
			.withBaseUrl(baseUrl)
			.withUsername(username)
			.withPassword(password)
			.withFamilyIds(familyIds)
			.withConnectTimeout(connectTimeout)
			.withReadTimeout(readTimeout);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(result.getIntegrationType()).isEqualTo(integrationType);
		assertThat(result.getInstanceType()).isEqualTo(instanceType);
		assertThat(result.getBaseUrl()).isEqualTo(baseUrl);
		assertThat(result.getUsername()).isEqualTo(username);
		assertThat(result.getPassword()).isEqualTo(password);
		assertThat(result.getFamilyIds()).isEqualTo(familyIds);
		assertThat(result.getConnectTimeout()).isEqualTo(connectTimeout);
		assertThat(result.getReadTimeout()).isEqualTo(readTimeout);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstanceEntity.create()).hasAllNullFieldsOrPropertiesExcept("connectTimeout", "readTimeout")
			.satisfies(instanceEntity -> {
				assertThat(instanceEntity.getConnectTimeout()).isZero();
				assertThat(instanceEntity.getReadTimeout()).isZero();
			});
		assertThat(new InstanceEntity()).hasAllNullFieldsOrPropertiesExcept("connectTimeout", "readTimeout")
			.satisfies(instanceEntity -> {
				assertThat(instanceEntity.getConnectTimeout()).isZero();
				assertThat(instanceEntity.getReadTimeout()).isZero();
			});
	}

}
