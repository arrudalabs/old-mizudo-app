package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@QuarkusTest
public class UsuarioDoMembroTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @BeforeEach
    @AfterEach
    void limparMembros(){
        apiTestSupport.execute(Usuario::apagarTodosOsUsuarios);
        apiTestSupport.execute(Membro::removerTodosMembros);
    }

    @Test
    void deveDefinirUsuarioParaUmMembroValido(){

        var membro = apiTestSupport.executeAndGet(()->Membro.novoMembro(UUID.randomUUID().toString()));

        String senha = UUID.randomUUID().toString();
        String username = UUID.randomUUID().toString();
        apiTestSupport
                .newAuthenticatedRequest()
                .log().everything()
                .contentType(ContentType.JSON)
                .body(Json.createObjectBuilder()
                        .add("username", username)
                        .add("senha", senha)
                        .add("confirmacaoSenha",senha)
                        .build().toString())
                .put("/resources/membros/{id}/user", Map.of("id",membro.id))
                .then()
                .log().everything()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("username", is(username))
                .body("senha", is("*****"));

    }


}
