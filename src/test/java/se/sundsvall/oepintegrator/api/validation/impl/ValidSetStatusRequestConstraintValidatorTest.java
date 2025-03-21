package se.sundsvall.oepintegrator.api.validation.impl;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;

@ExtendWith(MockitoExtension.class)
class ValidSetStatusRequestConstraintValidatorTest {

	@Mock
	private ConstraintValidatorContext constraintValidatorContextMock;

	@InjectMocks
	private ValidSetStatusRequestConstraintValidator validator;

	@Test
	void validSetStatusRequest() {
		final var setStatusRequest = new SetStatusRequest().withStatus("status");

		assertThat(validator.isValid(setStatusRequest, constraintValidatorContextMock)).isTrue();
	}

	@Test
	void validSetStatusRequestWithStatusId() {
		final var setStatusRequest = new SetStatusRequest().withStatusId(123);

		assertThat(validator.isValid(setStatusRequest, constraintValidatorContextMock)).isTrue();
	}

	@Test
	void invalidSetStatusRequest() {
		final var setStatusRequest = new SetStatusRequest();

		assertThat(validator.isValid(setStatusRequest, constraintValidatorContextMock)).isFalse();
	}
}
