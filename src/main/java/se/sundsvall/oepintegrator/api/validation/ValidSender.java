package se.sundsvall.oepintegrator.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import se.sundsvall.oepintegrator.api.validation.impl.ValidSenderConstraintValidator;

@Documented
@Target({
	ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidSenderConstraintValidator.class)
public @interface ValidSender {

	String message() default "all attributes are empty. One of the attributes must be set";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
