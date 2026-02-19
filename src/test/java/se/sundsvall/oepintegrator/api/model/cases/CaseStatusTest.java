package se.sundsvall.oepintegrator.api.model.cases;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

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
		final var id = 123;
		final var name = "name";
		final var status = "SUBMITTED";
		final var newExternalMessagesDisallowed = false;
		final var addExternalMessage = false;
		final var addInternalMessage = false;

		// Act
		final var bean = CaseStatus.create()
			.withId(id)
			.withName(name)
			.withNewExternalMessagesDisallowed(newExternalMessagesDisallowed)
			.withAddExternalMessage(addExternalMessage)
			.withAddInternalMessage(addInternalMessage)
			.withIsRestrictedAdminDeletable(false)
			.withStatus(status);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getName()).isEqualTo(name);
		assertThat(bean.getNewExternalMessagesDisallowed()).isEqualTo(newExternalMessagesDisallowed);
		assertThat(bean.getAddExternalMessage()).isEqualTo(addExternalMessage);
		assertThat(bean.getAddInternalMessage()).isEqualTo(addInternalMessage);
		assertThat(bean.getIsRestrictedAdminDeletable()).isFalse();
		assertThat(bean.getStatus()).isEqualTo(status);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CaseStatus.create()).hasAllNullFieldsOrProperties();
		assertThat(new CaseStatus()).hasAllNullFieldsOrProperties();
	}
}
