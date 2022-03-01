package io.github.arrudalabs.mizudo.resources.membros;


import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class EmailsDoMembroResourceTest {
    @Inject
    ApiTestSupport apiTestSupport;


    @BeforeEach
    @AfterEach
    public void limparMembros() {
        apiTestSupport.execute(() -> {
            Membro.removerTodosMembros();
        });
    }

    @Test
    @DisplayName("adicionar um email válido a um membro")
    public void test01() {

        var membroValido = apiTestSupport.executeAndGet(() -> {
            return Membro.novoMembro(UUID.randomUUID().toString());
        });

        var emailEsperado1 = "dearrudam1@gmail.com";
        var emailEsperado2 = "dearrudam2@gmail.com";
        var emailEsperado3 = "dearrudam3@gmail.com";

        Long membroId = membroValido.id;
        setEmailsDoMembroComSucesso(membroId, emailEsperado1)
                .body("$", hasItems(emailEsperado1));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(1));

        setEmailsDoMembroComSucesso(membroId, emailEsperado1, emailEsperado2)
                .body("$", hasItems(emailEsperado1, emailEsperado2));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(2));

        setEmailsDoMembroComSucesso(membroId, emailEsperado1, emailEsperado2, emailEsperado3)
                .body("$", hasItems(emailEsperado1, emailEsperado2, emailEsperado3));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(3));

        setEmailsDoMembroComSucesso(membroId)
                .body("$", hasSize(0));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(0));

    }

    private ValidatableResponse setEmailsDoMembroComSucesso(Long membroId, String... emails) {
        return setEmaisDoMembro(membroId, emails)
                .statusCode(Response.Status.OK.getStatusCode());
    }

    private ValidatableResponse setEmaisDoMembro(Long membroId, String... emails) {
        return apiTestSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(emails)
                .put("/resources/membros/{id}/emails", Map.of("id", membroId))
                .then();
    }

    private ValidatableResponse listarEmailsDoMembro(Long membroId) {
        return apiTestSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/resources/membros/{id}/emails", Map.of("id", membroId))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                ;
    }

    @ParameterizedTest(name = "{index} {0}")
    @DisplayName("não deve aceitar lista com e-mails inválidos para um dado membro")
    @MethodSource("test02Args")
    public void test02(String cenario, String email) {
        var membroId = apiTestSupport
                .executeAndGet(() -> Membro.novoMembro(UUID.randomUUID().toString()).id);
        setEmaisDoMembro(membroId, email)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    static Stream<Arguments> test02Args() {
        return Stream.of(
                Arguments.arguments(
                        "não permitir e-mail inválido",
                        UUID.randomUUID().toString()
                ),
                Arguments.arguments(
                        "não permitir e-mail em branco",
                        ""
                ),
                Arguments.arguments(
                        "não permitir e-mail nulo",
                        null
                )

        );
    }

}
