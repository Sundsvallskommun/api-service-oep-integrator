package se.sundsvall.oepintegrator.api.model.webmessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

class WebmessageTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> LocalDateTime.now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(Webmessage.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var id = 1234;
		final var direction = Direction.INBOUND;
		final var municipalityId = "municipalityId";
		final var familyId = "familyId";
		final var externalCaseId = "externalCaseId";
		final var message = "message";
		final var messageId = "messageId";
		final var sent = LocalDateTime.now();
		final var username = "username";
		final var firstName = "firstName";
		final var lastName = "lastName";
		final var email = "email";
		final var userId = "userId";
		final var attachments = List.<WebmessageAttachment>of();
		final var instance = "instance";

		// Act
		final var bean = Webmessage.create()
			.withId(id)
			.withDirection(direction)
			.withMunicipalityId(municipalityId)
			.withFamilyId(familyId)
			.withExternalCaseId(externalCaseId)
			.withMessage(message)
			.withMessageId(messageId)
			.withSent(sent)
			.withUsername(username)
			.withFirstName(firstName)
			.withLastName(lastName)
			.withEmail(email)
			.withUserId(userId)
			.withAttachments(attachments)
			.withInstance(instance);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getDirection()).isEqualTo(direction);
		assertThat(bean.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(bean.getFamilyId()).isEqualTo(familyId);
		assertThat(bean.getExternalCaseId()).isEqualTo(externalCaseId);
		assertThat(bean.getMessage()).isEqualTo(message);
		assertThat(bean.getMessageId()).isEqualTo(messageId);
		assertThat(bean.getSent()).isEqualTo(sent);
		assertThat(bean.getUsername()).isEqualTo(username);
		assertThat(bean.getFirstName()).isEqualTo(firstName);
		assertThat(bean.getLastName()).isEqualTo(lastName);
		assertThat(bean.getEmail()).isEqualTo(email);
		assertThat(bean.getUserId()).isEqualTo(userId);
		assertThat(bean.getAttachments()).isEqualTo(attachments);
		assertThat(bean.getInstance()).isEqualTo(instance);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Webmessage.create()).hasAllNullFieldsOrProperties();
		assertThat(new Webmessage()).hasAllNullFieldsOrProperties();
	}
}
