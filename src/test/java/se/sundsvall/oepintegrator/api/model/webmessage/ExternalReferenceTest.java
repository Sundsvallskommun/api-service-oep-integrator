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

class ExternalReferenceTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(ExternalReference.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var key = "key";
		final var value = "value";

		// Act
		final var result = ExternalReference.create()
			.withKey(key)
			.withValue(value);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getKey()).isEqualTo(key);
		assertThat(result.getValue()).isEqualTo(value);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(ExternalReference.create()).hasAllNullFieldsOrProperties();
		assertThat(new ExternalReference()).hasAllNullFieldsOrProperties();
	}

}
