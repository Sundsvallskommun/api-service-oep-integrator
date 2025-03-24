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

class PrincipalTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(Principal.class, allOf(
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
		final var userId = "userId";

		// Act
		final var bean = Principal.create()
			.withName(name)
			.withUserId(userId);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getName()).isEqualTo(name);
		assertThat(bean.getUserId()).isEqualTo(userId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(SetStatusRequest.create()).hasAllNullFieldsOrPropertiesExcept("statusId");
		assertThat(new SetStatusRequest()).hasAllNullFieldsOrPropertiesExcept("statusId");
	}
}
