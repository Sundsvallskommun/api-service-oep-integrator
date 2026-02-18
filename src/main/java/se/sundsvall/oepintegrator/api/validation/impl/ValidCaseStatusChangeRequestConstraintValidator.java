package se.sundsvall.oepintegrator.api.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;
import se.sundsvall.oepintegrator.api.validation.ValidCaseStatusChangeRequest;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ValidCaseStatusChangeRequestConstraintValidator implements ConstraintValidator<ValidCaseStatusChangeRequest, CaseStatusChangeRequest> {

	@Override
	public boolean isValid(final CaseStatusChangeRequest caseStatusChangeRequest, final ConstraintValidatorContext context) {
		return (isNotEmpty(caseStatusChangeRequest.getName()) || (nonNull(caseStatusChangeRequest.getId()) && caseStatusChangeRequest.getId() != 0));
	}
}
