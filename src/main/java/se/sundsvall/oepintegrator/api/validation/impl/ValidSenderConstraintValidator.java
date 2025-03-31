package se.sundsvall.oepintegrator.api.validation.impl;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isAllBlank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import se.sundsvall.oepintegrator.api.model.webmessage.Sender;
import se.sundsvall.oepintegrator.api.validation.ValidSender;

public class ValidSenderConstraintValidator implements ConstraintValidator<ValidSender, Sender> {

	private static final String CUSTOM_ERROR_MESSAGE = "only one of the attributes can be set at a time";

	@Override
	public boolean isValid(final Sender sender, final ConstraintValidatorContext context) {
		if (isNull(sender) || isAllBlank(sender.getAdministratorId(), sender.getPartyId(), sender.getUserId())) {
			return false;
		}

		return validateMutualExclusivity(context, sender);
	}

	private boolean validateMutualExclusivity(final ConstraintValidatorContext context, final Sender sender) {

		final var numberOfProvidedAttributes = asList(sender.getAdministratorId(), sender.getPartyId(), sender.getUserId())
			.stream()
			.filter(Objects::nonNull)
			.count();

		if (numberOfProvidedAttributes > 1) {
			useCustomMessageForValidation(context, CUSTOM_ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void useCustomMessageForValidation(final ConstraintValidatorContext constraintContext, final String message) {
		constraintContext.disableDefaultConstraintViolation();
		constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
