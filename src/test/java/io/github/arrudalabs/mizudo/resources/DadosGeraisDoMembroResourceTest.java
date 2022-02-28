package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.model.Membro;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class DadosGeraisDoMembroResourceTest {

    @Inject
    TestSupport testSupport;


    @BeforeEach
    @AfterEach
    public void removerMembros() {
        testSupport.execute(() -> {
            Membro.removerTodosMembros();
        });
    }

    @Test
    @DisplayName("deve adicionar dados gerais ao membro")
    @Order(1)
    public void teste01() {
        Membro membroPersistido =
        testSupport.executeAndGet(() -> {
            return Membro.novoMembro("Maximillian");
        });
        JsonObject payload = Json.createObjectBuilder()
                .add("dataNascimento", LocalDate.now().toString())
                .add("endereco",
                        Json.createObjectBuilder()
                                .add("logradouro", UUID.randomUUID().toString())
                                .add("numero", UUID.randomUUID().toString())
                                .add("complemento", UUID.randomUUID().toString())
                                .add("localidade", UUID.randomUUID().toString())
                                .add("bairro", UUID.randomUUID().toString())
                                .add("uf", UUID.randomUUID().toString())
                                .add("cep", UUID.randomUUID().toString())
                                .build()
                )
                .build();

        testSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString())
                .put("/resources/membros/{id}/dados-gerais", Map.of("id", membroPersistido.id))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("dataNascimento", equalTo(payload.getString("dataNascimento")))
                .body("endereco.logradouro", equalTo(payload.getJsonObject("endereco").getString("logradouro")))
                .body("endereco.numero", equalTo(payload.getJsonObject("endereco").getString("numero")))
                .body("endereco.complemento", equalTo(payload.getJsonObject("endereco").getString("complemento")))
                .body("endereco.localidade", equalTo(payload.getJsonObject("endereco").getString("localidade")))
                .body("endereco.bairro", equalTo(payload.getJsonObject("endereco").getString("bairro")))
                .body("endereco.uf", equalTo(payload.getJsonObject("endereco").getString("uf")))
                .body("endereco.cep", equalTo(payload.getJsonObject("endereco").getString("cep")))
        ;


        testSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/resources/membros/{id}/dados-gerais", Map.of("id", membroPersistido.id))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("dataNascimento", equalTo(payload.getString("dataNascimento")))
                .body("endereco.logradouro", equalTo(payload.getJsonObject("endereco").getString("logradouro")))
                .body("endereco.numero", equalTo(payload.getJsonObject("endereco").getString("numero")))
                .body("endereco.complemento", equalTo(payload.getJsonObject("endereco").getString("complemento")))
                .body("endereco.localidade", equalTo(payload.getJsonObject("endereco").getString("localidade")))
                .body("endereco.bairro", equalTo(payload.getJsonObject("endereco").getString("bairro")))
                .body("endereco.uf", equalTo(payload.getJsonObject("endereco").getString("uf")))
                .body("endereco.cep", equalTo(payload.getJsonObject("endereco").getString("cep")))
        ;


    }


    @Test
    @DisplayName("deve adicionar dados gerais em branco ao membro")
    @Order(1)
    public void teste02() {
        Membro membroPersistido =
        testSupport.executeAndGet(() -> {
            return Membro.novoMembro("Maximillian");
        });

        JsonObject payload = Json.createObjectBuilder()
                .build();

        testSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString())
                .put("/resources/membros/{id}/dados-gerais", Map.of("id", membroPersistido.id))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("isEmpty()", is(true))
        ;

        testSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/resources/membros/{id}/dados-gerais", Map.of("id", membroPersistido.id))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("isEmpty()", is(true))
        ;
    }
}
