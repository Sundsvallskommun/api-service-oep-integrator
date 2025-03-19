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

class WebmessageAttachmentDataTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(WebmessageAttachmentData.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var data = new byte[10];

		// Act
		final var bean = WebmessageAttachmentData.create()
			.withData(data);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getData()).isEqualTo(data);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebmessageAttachmentData.create()).hasAllNullFieldsOrProperties();
		assertThat(new WebmessageAttachmentData()).hasAllNullFieldsOrProperties();
	}

}
