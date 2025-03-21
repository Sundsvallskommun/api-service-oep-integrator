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

class SetStatusRequestTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(SetStatusRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var status = "status";
		final var statusId = 123;
		final var principal = Principal.create().withUserId("userId").withName("name");

		// Act
		final var bean = SetStatusRequest.create()
			.withStatus(status)
			.withStatusId(statusId)
			.withPrincipal(principal);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getStatus()).isEqualTo(status);
		assertThat(bean.getStatusId()).isEqualTo(statusId);
		assertThat(bean.getPrincipal()).isEqualTo(principal);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(SetStatusRequest.create()).hasAllNullFieldsOrPropertiesExcept("statusId");
		assertThat(new SetStatusRequest()).hasAllNullFieldsOrPropertiesExcept("statusId");
	}

}
