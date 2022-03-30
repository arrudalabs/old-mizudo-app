package io.github.arrudalabs.mizudo.resources.token;

import com.github.javafaker.Faker;
import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.github.arrudalabs.mizudo.services.GeradorDeHash;
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

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@QuarkusTest
class TokenResourceTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @Inject
    GeradorDeHash geradorDeHash;

    static Faker faker = new Faker();

    @BeforeEach
    @AfterEach
    void apagarMembrosEUsuarios() {
        apiTestSupport.execute(() -> {
            Usuario.apagarTodosOsUsuarios();
            Membro.removerTodosMembros();
        });
    }

    @Test
    void testDeveCriarUmTokenValido() {
        //criar cenario
        var username = faker.internet().emailAddress();
        var senha = faker.internet().password(4, 8);
        apiTestSupport.execute(() -> {
            Membro membro = Membro.novoMembro(faker.name().fullName());
            Usuario.definirUsuarioParaMembroInformado(membro, username, senha, geradorDeHash);
        });

        apiTestSupport
                .newNonAuthenticatedRequest()
                .log().everything()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "username": "%s",
                            "senha": "%s"        
                        }
                        """.formatted(username, senha))
                .post("/resources/token")
                .then()
                .log().everything()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("access_token", is(notNullValue()))
                .body("refresh_token", is(notNullValue()));

    }

    @ParameterizedTest(name = "Caso {index} => {0}")
    @MethodSource("payloadsInvalidos")
    void testCasosComFalha(String cenario, String payloadInvalido, Response.Status statusCode) {
        apiTestSupport
                .newNonAuthenticatedRequest()
                .log().everything()
                .contentType(ContentType.JSON)
                .body(payloadInvalido)
                .post("/resources/token")
                .then()
                .log().everything()
                .statusCode(statusCode.getStatusCode());

    }

    static Stream<Arguments> payloadsInvalidos(){
        return Stream.of(
                arguments(
                        "deve retornar BAD_REQUEST quando o username for null",
                        """
                        {
                            "username": null,
                            "senha": "%s"        
                        }
                        """.formatted(faker.internet().password(4, 8)),
                        Response.Status.BAD_REQUEST
                ),
                arguments(
                        "deve retornar BAD_REQUEST quando o username estiver em vazio",
                        """
                        {
                            "username": "",
                            "senha": "%s"        
                        }
                        """.formatted(faker.internet().password(4, 8)),
                        Response.Status.BAD_REQUEST
                ),
                arguments(
                        "deve retornar BAD_REQUEST quando o username estiver em branco",
                        """
                        {
                            "username": " ",
                            "senha": "%s"        
                        }
                        """.formatted(faker.internet().password(4, 8)),
                        Response.Status.BAD_REQUEST
                ),
                arguments(
                        "deve retornar BAD_REQUEST quando o username não é informado",
                        """
                        {
                            "senha": "%s"        
                        }
                        """.formatted(faker.internet().password(4, 8)),
                        Response.Status.BAD_REQUEST
                ),
                arguments(
                        "deve retornar BAD_REQUEST quando a senha não for informada",
                        """
                        {
                            "username": "%s"        
                        }
                        """.formatted(faker.internet().emailAddress()),
                        Response.Status.BAD_REQUEST
                ),
                arguments(
                        "deve retornar BAD_REQUEST quando a senha for vazia",
                        """
                        {
                            "username": "%s",
                            "senha": ""        
                        }
                        """.formatted(faker.internet().emailAddress()),
                        Response.Status.BAD_REQUEST
                ),
                arguments(
                        "deve retornar BAD_REQUEST quando a senha estiver em branco",
                        """
                        {
                            "username": "%s",
                            "senha": " "        
                        }
                        """.formatted(faker.internet().emailAddress()),
                        Response.Status.BAD_REQUEST
                ),
                arguments(
                        "deve retornar UNAUTHORIZED quando username e senha não forem válidos",
                        """
                        {
                            "username": "%s",
                            "senha": "%s"        
                        }
                        """.formatted(faker.internet().emailAddress(),faker.internet().password(4, 8)),
                        Response.Status.UNAUTHORIZED
                )
        );
    }

}