package io.github.arrudalabs.mizudo.validation;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;

import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SuportaValidacaoValidatorTest {

    @Inject
    Validator validator;

    @Test
    void test() {
        assertFalse(validator.validate(new Pojo(null,"ewrw")).isEmpty());
    }


    @SuportaValidacao(
            message = "Pojo invalido - atributos diferentes",
            classeValidadora = Pojo.PojoValidator.class
    )
    static record Pojo(@NotBlank String atributo1,
                       @NotBlank String atributo2) {

        public static class PojoValidator implements Predicate<Pojo> {

            @Override
            public boolean test(Pojo o) {
                return Objects.nonNull(o)
                        && Objects.equals(o.atributo1, o.atributo2);
            }
        }

    }
}