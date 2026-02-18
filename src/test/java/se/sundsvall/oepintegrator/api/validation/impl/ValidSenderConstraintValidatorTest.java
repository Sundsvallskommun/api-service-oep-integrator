package se.sundsvall.oepintegrator.api.validation.impl;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidSenderConstraintValidatorTest {

	@Mock
	private ConstraintValidatorContext constraintValidatorContextMock;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock;

	@InjectMocks
	private ValidSenderConstraintValidator validator;

	@Test
	void validSender() {
		final var sender = Sender.create()
			.withAdministratorId("administratorId");

		assertThat(validator.isValid(sender, constraintValidatorContextMock)).isTrue();

		verifyNoInteractions(constraintValidatorContextMock, constraintViolationBuilderMock);
	}

	@Test
	void invalidSenderAllAttributesNull() {
		final var sender = Sender.create();

		assertThat(validator.isValid(sender, constraintValidatorContextMock)).isFalse();

		verifyNoInteractions(constraintValidatorContextMock, constraintViolationBuilderMock);
	}

	@Test
	void invalidSenderAllAttributesEmpty() {
		final var sender = Sender.create()
			.withAdministratorId("")
			.withPartyId("")
			.withUserId("");

		assertThat(validator.isValid(sender, constraintValidatorContextMock)).isFalse();

		verifyNoInteractions(constraintValidatorContextMock, constraintViolationBuilderMock);
	}

	@Test
	void invalidSenderMoreThanOneAttributesSet() {
		final var sender = Sender.create()
			.withAdministratorId("administratorId")
			.withUserId("userId");

		when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilderMock);

		assertThat(validator.isValid(sender, constraintValidatorContextMock)).isFalse();

		verify(constraintValidatorContextMock).buildConstraintViolationWithTemplate("only one of the attributes can be set at a time");
		verify(constraintViolationBuilderMock).addConstraintViolation();
	}
}
