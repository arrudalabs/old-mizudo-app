package io.github.arrudalabs.mizudo.validation;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SuportaValidacaoValidatorTest {

    @Inject
    Validator validator;

    @Test
    @DisplayName("o TipoA deveria estar inválido")
    void test1() {
        assertFalse(validator.validate(new TipoA(null, "ewrw")).isEmpty());
    }

    @Test
    @DisplayName("o TipoA deveria estar válido")
    void test2() {
        assertTrue(validator.validate(new TipoA("ewrw", "ewrw")).isEmpty());
    }


    @SuportaValidacao(
            message = "Pojo invalido - atributos diferentes",
            classeValidadora = TipoA.PojoValidator.class
    )
    static record TipoA(@NotBlank String atributo1,
                        @NotBlank String atributo2) {

        public static class PojoValidator implements Validacao {

            @Override
            public boolean estahValido(Object object) {
                if(object instanceof TipoA o)
                    return Objects.equals(o.atributo1, o.atributo2);
                return false;
            }
        }

    }
}