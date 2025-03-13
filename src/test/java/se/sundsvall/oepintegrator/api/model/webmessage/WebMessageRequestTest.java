package se.sundsvall.oepintegrator.api.model.webmessage;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class WebMessageRequestTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(WebMessageRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var sender = Sender.create().withUserId("userId");
		final var externalReferences = List.of(ExternalReference.create());
		final var message = "message";
		// Act
		final var result = WebMessageRequest.create()
			.withSender(sender)
			.withMessage(message)
			.withExternalReferences(externalReferences);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getSender()).isEqualTo(sender);
		assertThat(result.getMessage()).isEqualTo(message);
		assertThat(result.getExternalReferences()).isEqualTo(externalReferences);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebMessageRequest.create()).hasAllNullFieldsOrProperties();
		assertThat(new WebMessageRequest()).hasAllNullFieldsOrProperties();
	}

}
