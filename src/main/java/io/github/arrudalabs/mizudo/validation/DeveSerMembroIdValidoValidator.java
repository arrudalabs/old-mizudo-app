package io.github.arrudalabs.mizudo.validation;

import io.github.arrudalabs.mizudo.model.Membro;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@ApplicationScoped
public class DeveSerMembroIdValidoValidator implements ConstraintValidator<DeveSerMembroIdValido, Long> {
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && Membro.findByIdOptional(value).isPresent();
    }
}
