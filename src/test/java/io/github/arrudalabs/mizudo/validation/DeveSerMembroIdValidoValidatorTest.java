package io.github.arrudalabs.mizudo.validation;

import io.github.arrudalabs.mizudo.TestSupport;
import io.github.arrudalabs.mizudo.model.Membro;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import javax.transaction.SystemException;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class DeveSerMembroIdValidoValidatorTest {

    @Inject
    TestSupport testSupport;

    @Inject
    DeveSerMembroIdValidoValidator validator;

    @Test
    @DisplayName("testando isValid() quando o membroId for válido")
    public void testIsValidWhenMembroIdIsValid() {
        var membroId = testSupport.executeAndGet(() -> Membro.novoMembro(UUID.randomUUID().toString()).id);
        testIsValid(membroId, true);
    }

    @AfterEach
    public void apagarMembrosCriadosNesteTeste(){
        testSupport.execute(Membro::removerTodosMembros);
    }

    @ParameterizedTest(name = "{index} {0}")
    @DisplayName("testando isValid() quando o membroId for inválido")
    @MethodSource("testIsValidWhenMembroIdIsInvalidArgs")
    public void testIsValidWhenMembroIdIsInvalid(String cenario,
                                                 Long membroId) {
        testIsValid(membroId, false);
    }

    private void testIsValid(Long membroId, boolean ehValido) {
        assertThat(validator.isValid(membroId, null), is(ehValido));
    }

    static Stream<Arguments> testIsValidWhenMembroIdIsInvalidArgs() throws SystemException {

        return Stream.of(
                Arguments.arguments(
                        "quando o membroId for inválido",
                        -1l
                ),
                Arguments.arguments(
                        "quando o membroId for nulo",
                        null
                )
        );
    }

}