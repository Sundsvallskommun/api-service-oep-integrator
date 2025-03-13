package se.sundsvall.oepintegrator.api.model.webmessage;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class SenderTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(Sender.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var userId = "joe01doe";

		// Act
		final var result = Sender.create()
			.withUserId(userId);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getUserId()).isEqualTo(userId);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Sender.create()).hasAllNullFieldsOrProperties();
		assertThat(new Sender()).hasAllNullFieldsOrProperties();
	}
}
