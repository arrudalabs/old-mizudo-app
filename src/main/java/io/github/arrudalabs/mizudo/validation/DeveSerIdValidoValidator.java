package io.github.arrudalabs.mizudo.validation;

import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Optional;

public class DeveSerIdValidoValidator implements ConstraintValidator<DeveSerIdValido, Serializable> {

    private DeveSerIdValido constraintAnnotation;

    @Override
    public void initialize(DeveSerIdValido constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
                .map(v -> this.isValid(v))
                .orElse(false);
    }

    private Boolean isValid(Serializable id) {
        Class<?> entityClass = this.constraintAnnotation.entityClass();
        return JpaOperations.INSTANCE
                .findByIdOptional(entityClass, id)
                .isPresent();
    }
}
