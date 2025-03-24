package se.sundsvall.oepintegrator.api.validation.impl;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;
import se.sundsvall.oepintegrator.api.validation.ValidSetStatusRequest;

public class ValidSetStatusRequestConstraintValidator implements ConstraintValidator<ValidSetStatusRequest, SetStatusRequest> {

	@Override
	public boolean isValid(final SetStatusRequest setStatusRequest, final ConstraintValidatorContext context) {
		return (isNotEmpty(setStatusRequest.getStatus()) || setStatusRequest.getStatusId() != 0);
	}
}
