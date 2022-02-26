package io.github.arrudalabs.mizudo.validation;

import io.github.arrudalabs.mizudo.model.Membro;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NaoDeveConterMembroCOmMesmoNomeValidator implements ConstraintValidator<NaoDeveConterMembroComMesmoNome, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(Objects.isNull(value))
            return true;
        return Membro.procurarPorNome(value).isEmpty();
    }
}
