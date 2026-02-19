package se.sundsvall.oepintegrator.api.validation.impl;

import jakarta.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidExternalReferencesConstraintValidatorTest {

	@Mock
	private ConstraintValidatorContext constraintValidatorContextMock;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock;

	@InjectMocks
	private ValidExternalReferencesConstraintValidator validator;

	@Test
	void validExternalReferences() {
		final var externalReferences = List.of(ExternalReference.create().withKey("key").withValue("value"));

		assertThat(validator.isValid(externalReferences, constraintValidatorContextMock)).isTrue();

		verifyNoInteractions(constraintValidatorContextMock, constraintViolationBuilderMock);
	}

	@Test
	void invalidExternalReferences() {
		final var externalReferences = List.of(ExternalReference.create().withKey("key").withValue(null));

		assertThat(validator.isValid(externalReferences, constraintValidatorContextMock)).isFalse();

		verifyNoInteractions(constraintValidatorContextMock, constraintViolationBuilderMock);
	}

	@Test
	void emptyExternalReferences() {
		final List<ExternalReference> externalReferences = Collections.emptyList();

		assertThat(validator.isValid(externalReferences, constraintValidatorContextMock)).isFalse();

		verifyNoInteractions(constraintValidatorContextMock, constraintViolationBuilderMock);
	}

	@Test
	void nonNumericExternalReferenceFlowInstanceId() {
		when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilderMock);

		final var externalReferences = List.of(ExternalReference.create().withKey("flowInstanceId").withValue("not_numeric"));

		assertThat(validator.isValid(externalReferences, constraintValidatorContextMock)).isFalse();

		verify(constraintValidatorContextMock).disableDefaultConstraintViolation();
		verify(constraintValidatorContextMock).buildConstraintViolationWithTemplate("element with key 'flowInstanceId' must have value of numeric type");
		verify(constraintViolationBuilderMock).addConstraintViolation();
	}
}
