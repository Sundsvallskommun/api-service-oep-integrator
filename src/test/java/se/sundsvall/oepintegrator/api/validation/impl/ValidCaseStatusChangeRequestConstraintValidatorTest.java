package se.sundsvall.oepintegrator.api.validation.impl;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidCaseStatusChangeRequestConstraintValidatorTest {

	@Mock
	private ConstraintValidatorContext constraintValidatorContextMock;

	@InjectMocks
	private ValidCaseStatusChangeRequestConstraintValidator validator;

	@Test
	void validCaseStatusChangeRequest() {
		final var setStatusRequest = new CaseStatusChangeRequest().withName("name");

		assertThat(validator.isValid(setStatusRequest, constraintValidatorContextMock)).isTrue();
	}

	@Test
	void validCaseStatusChangeRequestWithStatusId() {
		final var setStatusRequest = new CaseStatusChangeRequest().withId(123);

		assertThat(validator.isValid(setStatusRequest, constraintValidatorContextMock)).isTrue();
	}

	@Test
	void invalidCaseStatusChangeRequest() {
		final var setStatusRequest = new CaseStatusChangeRequest();

		assertThat(validator.isValid(setStatusRequest, constraintValidatorContextMock)).isFalse();
	}
}
