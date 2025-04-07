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

class ConfirmDeliveryRequestTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(ConfirmDeliveryRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var caseId = "123456";
		final var delivered = true;
		final var logMessage = "The case was delivered successfully";
		final var system = "ByggR";

		// Act
		final var result = ConfirmDeliveryRequest.create()

			.withDelivered(delivered)
			.withCaseId(caseId)
			.withLogMessage(logMessage)
			.withSystem(system);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getCaseId()).isEqualTo(caseId);
		assertThat(result.isDelivered()).isEqualTo(delivered);
		assertThat(result.getLogMessage()).isEqualTo(logMessage);
		assertThat(result.getSystem()).isEqualTo(system);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(ConfirmDeliveryRequest.create()).hasAllNullFieldsOrPropertiesExcept("delivered")
			.satisfies(confirmDeliveryRequest -> assertThat(confirmDeliveryRequest.isDelivered()).isFalse());
		assertThat(new ConfirmDeliveryRequest()).hasAllNullFieldsOrPropertiesExcept("delivered")
			.satisfies(confirmDeliveryRequest -> assertThat(confirmDeliveryRequest.isDelivered()).isFalse());
	}
}
