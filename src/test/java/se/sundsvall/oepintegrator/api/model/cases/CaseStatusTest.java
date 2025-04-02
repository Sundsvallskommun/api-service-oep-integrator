package se.sundsvall.oepintegrator.api.model.cases;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class CaseStatusTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(CaseStatus.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var name = "name";
		final var contentType = "contentType";

		// Act
		final var bean = CaseStatus.create()
			.withName(name);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CaseStatus.create()).hasAllNullFieldsOrProperties();
		assertThat(new CaseStatus()).hasAllNullFieldsOrProperties();
	}
}
