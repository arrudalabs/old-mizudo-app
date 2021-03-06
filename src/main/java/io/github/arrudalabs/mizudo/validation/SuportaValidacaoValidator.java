package io.github.arrudalabs.mizudo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

public class SuportaValidacaoValidator implements ConstraintValidator<SuportaValidacao, Object> {

    private SuportaValidacao constraintAnnotation;

    @Override
    public void initialize(SuportaValidacao constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.constraintAnnotation=constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            return this.constraintAnnotation
                    .classeValidadora()
                    .getConstructor(new Class[0])
                    .newInstance(new Object[0]).estahValido(value);
        } catch (IllegalAccessException
                | NoSuchMethodException
                | InstantiationException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
