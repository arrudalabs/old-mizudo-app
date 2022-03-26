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

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class TokenResourceTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @Inject
    GeradorDeHash geradorDeHash;

    Faker faker = new Faker();

    @BeforeEach
    @AfterEach
    void apagarMembrosEUsuarios() {
        apiTestSupport.execute(() -> {
            Usuario.apagarTodosOsUsuarios();
            Membro.removerTodosMembros();
        });
    }

    @Test
    void testDeveCriarUmToken() {
        //criar cenario
        var username = faker.internet().emailAddress();
        var senha = faker.internet().password(4, 8);
        apiTestSupport.execute(() -> {
            Membro membro = Membro.novoMembro(faker.name().fullName());
            Usuario.definirUsuario(membro, username, senha, geradorDeHash);
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
                .body("access_token", is(notNullValue()));

    }

}