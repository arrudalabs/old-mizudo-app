package io.github.arrudalabs.mizudo.validation;

import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Optional;

@ApplicationScoped
public class DeveSerIdValidoValidator implements ConstraintValidator<DeveSerIdValido, Serializable> {

    private DeveSerIdValido constraintAnnotation;

    @Override
    public void initialize(DeveSerIdValido constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
                .map(id -> {
                    Class<?> entityClass = this.constraintAnnotation.entityClass();
                    return JpaOperations.INSTANCE
                            .findByIdOptional(entityClass, id)
                            .isPresent();
                }).orElse(false);
    }
}
