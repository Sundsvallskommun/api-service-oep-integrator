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

class SetStatusResponseTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(SetStatusResponse.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var eventId = 123;

		// Act
		final var bean = SetStatusResponse.create()
			.withEventId(eventId);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getEventId()).isEqualTo(eventId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(SetStatusResponse.create()).hasAllNullFieldsOrProperties();
		assertThat(new SetStatusResponse()).hasAllNullFieldsOrProperties();
	}

}
