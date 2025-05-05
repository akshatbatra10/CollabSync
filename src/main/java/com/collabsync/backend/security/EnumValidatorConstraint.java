package com.collabsync.backend.security;

import com.collabsync.backend.common.annotations.EnumValidator;
import jakarta.validation.ConstraintValidator;

import java.util.Arrays;

public class EnumValidatorConstraint implements ConstraintValidator<EnumValidator, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) return true;

        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equalsIgnoreCase(value));
    }
}
