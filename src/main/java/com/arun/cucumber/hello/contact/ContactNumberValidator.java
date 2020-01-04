package com.arun.cucumber.hello.contact;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumberConstraint, Integer> {

    private static final String regex = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";

    @Override
    public void initialize(ContactNumberConstraint contactNumber) {
        // initialize
    }

    @Override
    public boolean isValid(Integer number, ConstraintValidatorContext cxt) {

        if (number == null) {
            return false;
        }

        String numberStr = String.valueOf(number);

        return (numberStr.matches(regex));

    }

}
