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

class CaseStatusChangeRequestTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(CaseStatusChangeRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var id = 123;
		final var name = "name";
		final var principal = Principal.create().withUserId("userId").withName("name");

		// Act
		final var bean = CaseStatusChangeRequest.create()
			.withId(id)
			.withName(name)
			.withPrincipal(principal);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getName()).isEqualTo(name);
		assertThat(bean.getPrincipal()).isEqualTo(principal);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CaseStatusChangeRequest.create()).hasAllNullFieldsOrProperties();
		assertThat(new CaseStatusChangeRequest()).hasAllNullFieldsOrProperties();
	}
}
