package dev.abykov.cloudmarketplace.menu.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullableNotBlankValidator.class)
public @interface NullableNotBlank {

    String message() default "Field must be null or contain non-blank text";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
