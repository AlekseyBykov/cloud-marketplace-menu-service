package dev.abykov.cloudmarketplace.menu.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * Validator for {@link NullableNotBlank}.
 */
public class NullableNotBlankValidator implements ConstraintValidator<NullableNotBlank, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || StringUtils.hasText(value);
    }
}
