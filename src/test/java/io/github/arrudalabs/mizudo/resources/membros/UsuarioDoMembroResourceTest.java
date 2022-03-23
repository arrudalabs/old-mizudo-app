package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@QuarkusTest
public class UsuarioDoMembroResourceTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @BeforeEach
    @AfterEach
    void limparMembros() {
        apiTestSupport.execute(Usuario::apagarTodosOsUsuarios);
        apiTestSupport.execute(Membro::removerTodosMembros);
    }

    @Test
    void deveDefinirUsuarioParaUmMembroValido() {

        var membro = apiTestSupport.executeAndGet(() -> Membro.novoMembro(UUID.randomUUID().toString()));

        String senha = UUID.randomUUID().toString();
        String username = UUID.randomUUID().toString();
        apiTestSupport
                .newAuthenticatedRequest()
                .log().everything()
                .contentType(ContentType.JSON)
                .body(
                        """
                                {
                                    "username": "%s",
                                    "senha": "%s",
                                    "confirmacaoSenha": "%s"     
                                }
                                """.formatted(username, senha, senha))
                .put("/resources/membros/{id}/user", Map.of("id", membro.id))
                .then()
                .log().everything()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("username", is(username))
                .body("senha", is("*****"));

        assertThat("Não foi persistido o usuário do membro.", Usuario.buscarUsuariosDoMembro(membro).count(), is(1L));
        assertThat("Não foi persistido o usuário do membro com o username informado.", Usuario.buscarPorUsername(username), notNullValue());
    }

    @ParameterizedTest(name = "{index} - deve retornar HTTP Status 400 - {0}")
    @MethodSource("invalidInputs")
    void naoDeveDefinirUsuarioParaUmMembroValido(String cenario, String payload) {

        var membro = apiTestSupport.executeAndGet(() -> Membro.novoMembro(UUID.randomUUID().toString()));

        apiTestSupport
                .newAuthenticatedRequest()
                .log().everything()
                .contentType(ContentType.JSON)
                .body(payload)
                .put("/resources/membros/{id}/user", Map.of("id", membro.id))
                .then()
                .log().everything()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());


    }

    static Stream<Arguments> invalidInputs(){
        return Stream.of(
                arguments(
                        "quando username for omitido",
                        """
                        {
                            "senha": "abcd",
                            "confirmacaoSenha": "abcd"
                        }
                        """
                ),
                arguments(
                        "quando username for vazio",
                        """
                        {
                            "username": "",
                            "senha": "abcd",
                            "confirmacaoSenha": "abcd"
                        }
                        """
                ),
                arguments(
                        "quando username estiver em branco",
                        """
                        {
                            "username": " ",
                            "senha": "abcd",
                            "confirmacaoSenha": "abcd"
                        }
                        """
                ),
                arguments(
                        "quando senha for omitida",
                        """
                        {
                            "username": "ze"
                            "senha": "abcd",
                            "confirmacaoSenha": "abcd"
                        }
                        """
                ),
                arguments(
                        "quando senha estiver vazia",
                        """
                        {
                            "username": "ze"
                            "senha": "",
                            "confirmacaoSenha": ""
                        }
                        """
                ),
                arguments(
                        "quando senha estiver em branco",
                        """
                        {
                            "username": " ",
                            "senha": " ",
                            "confirmacaoSenha": " "
                        }
                        """
                ),
                arguments(
                        "quando a senha e confirmacaoSenha não forem iguais",
                        """
                        {
                            "username": "ze",
                            "senha": "234234",
                            "confirmacaoSenha": "fjvdfvfdn"
                        }
                        """
                )
        );
    }

}
