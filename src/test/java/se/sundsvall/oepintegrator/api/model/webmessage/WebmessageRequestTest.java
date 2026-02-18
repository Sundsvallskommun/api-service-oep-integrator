package se.sundsvall.oepintegrator.api.model.webmessage;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

class WebmessageRequestTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(WebmessageRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var message = "message";
		final var externalReferences = List.of(ExternalReference.create());
		final var sender = Sender.create();

		// Act
		final var bean = WebmessageRequest.create()
			.withMessage(message)
			.withExternalReferences(externalReferences)
			.withSender(sender);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getMessage()).isEqualTo(message);
		assertThat(bean.getExternalReferences()).isEqualTo(externalReferences);
		assertThat(bean.getSender()).isEqualTo(sender);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebmessageRequest.create()).hasAllNullFieldsOrProperties();
		assertThat(new WebmessageRequest()).hasAllNullFieldsOrProperties();
	}

}
