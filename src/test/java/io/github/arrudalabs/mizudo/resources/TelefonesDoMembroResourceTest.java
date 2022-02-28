package io.github.arrudalabs.mizudo.resources;


import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Telefone;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@QuarkusTest
public class TelefonesDoMembroResourceTest {

    @Inject
    TestSupport testSupport;

    @BeforeEach
    @AfterEach
    public void apagarMembros() {
        testSupport.execute(Membro::removerTodosMembros);
    }

    @Test
    @DisplayName("deve definir telefones para o membro")
    public void test01() {

        var membroId = testSupport.executeAndGet(() -> Membro.novoMembro(UUID.randomUUID().toString()).id);

        var telefone1 = Telefone.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        var telefone2 = Telefone.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        assertThat("falha ao submeter o telefone",
                definirTelefones(membroId, telefone1)
                        .statusCode(Response.Status.OK.getStatusCode())
                        .extract()
                        .as(new TypeRef<List<Telefone>>() {
                        }), hasItems(telefone1));

        assertThat("não foi persistido corretamente o telefone",
                getTelefones(membroId)
                        .statusCode(Response.Status.OK.getStatusCode())
                        .extract()
                        .as(new TypeRef<List<Telefone>>() {
                        }), hasItems(telefone1));

        assertThat("falha ao submeter o telefone",
                definirTelefones(membroId, telefone1, telefone2)
                        .statusCode(Response.Status.OK.getStatusCode())
                        .extract()
                        .as(new TypeRef<List<Telefone>>() {
                        }), hasItems(telefone1, telefone2));

        assertThat("não foi persistido corretamente o telefone",
                getTelefones(membroId)
                        .statusCode(Response.Status.OK.getStatusCode())
                        .extract()
                        .as(new TypeRef<List<Telefone>>() {
                        }), hasItems(telefone1, telefone2));


    }

    private ValidatableResponse definirTelefones(Long membroId, Telefone... telefones) {
        return testSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(telefones)
                .put("/resources/membros/{id}/telefones", membroId)
                .then();
    }

    private ValidatableResponse getTelefones(Long membroId) {
        return testSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/resources/membros/{id}/telefones", membroId)
                .then();
    }

    @ParameterizedTest(name = "{index} {0}")
    @DisplayName("não deve aceitar lista com telefones inválidos para um dado membro")
    @MethodSource("test02Args")
    public void test02(String cenario, Telefone telefone) {
        var membroId = testSupport
                .executeAndGet(() -> Membro.novoMembro(UUID.randomUUID().toString()).id);
        definirTelefones(membroId,telefone)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    static Stream<Arguments> test02Args(){
        return Stream.of(
                Arguments.arguments(
                        "não permitir telefone vazio",
                        new Telefone()
                ),
                Arguments.arguments(
                        "não permitir telefone sem numero",
                        Telefone.of(null,UUID.randomUUID().toString())
                ),
                Arguments.arguments(
                        "não permitir instancia nula de telefone",
                        null
                )

        );
    }

}
