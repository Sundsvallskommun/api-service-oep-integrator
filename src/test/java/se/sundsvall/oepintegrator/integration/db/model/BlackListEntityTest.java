package se.sundsvall.oepintegrator.integration.db.model;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;

class BlackListEntityTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(BlackListEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var familyId = "familyId";
		final var id = "id";
		final var instanceType = EXTERNAL;
		final var municipalityId = "municipalityId";

		// Act
		final var result = BlackListEntity.create()
			.withFamilyId(familyId)
			.withId(id)
			.withInstanceType(instanceType)
			.withMunicipalityId(municipalityId);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getFamilyId()).isEqualTo(familyId);
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getInstanceType()).isEqualTo(instanceType);
		assertThat(result.getMunicipalityId()).isEqualTo(municipalityId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(BlackListEntity.create()).hasAllNullFieldsOrProperties();
		assertThat(new BlackListEntity()).hasAllNullFieldsOrProperties();
	}
}
