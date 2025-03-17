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

class WebmessageAttachmentTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(WebmessageAttachment.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var attachmentId = 1234;
		final var name = "name";
		final var mimeType = "text/plain";
		final var extension = ".txt";

		// Act
		final var bean = WebmessageAttachment.create()
			.withExtension(extension)
			.withAttachmentId(attachmentId)
			.withName(name)
			.withMimeType(mimeType);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getAttachmentId()).isEqualTo(attachmentId);
		assertThat(bean.getName()).isEqualTo(name);
		assertThat(bean.getExtension()).isEqualTo(extension);
		assertThat(bean.getMimeType()).isEqualTo(mimeType);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebmessageAttachment.create()).hasAllNullFieldsOrProperties();
		assertThat(new WebmessageAttachment()).hasAllNullFieldsOrProperties();
	}
}
