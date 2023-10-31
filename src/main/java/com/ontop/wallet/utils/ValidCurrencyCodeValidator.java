package com.ontop.wallet.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Currency;
import java.util.Set;

public class ValidCurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {

    private Boolean isOptional;

    @Override
    public void initialize(ValidCurrencyCode validCurrencyCode) {
        this.isOptional = validCurrencyCode.optional();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (isOptional && value == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

        boolean containsIsoCode = false;

        Set<Currency> currencies = Currency.getAvailableCurrencies();
        try {
            containsIsoCode = currencies.contains(Currency.getInstance(value));
        } catch (IllegalArgumentException e) {
        }
        return containsIsoCode;
    }
}