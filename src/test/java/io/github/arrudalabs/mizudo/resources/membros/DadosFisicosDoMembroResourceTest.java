package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DadosFisicosDoMembroResourceTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @BeforeEach
    @AfterEach
    public void removerMembros() {
        apiTestSupport.execute(() -> {
            Membro.removerTodosMembros();
        });
    }


    @Test
    @DisplayName("deve adicionar os dados físicos do membro")
    public void test01() {

        Long membroId = apiTestSupport.executeAndGet(() -> {
            return Membro.novoMembro(UUID.randomUUID().toString()).id;
        });

        JsonObject payload = Json.createObjectBuilder()
                .add("altura", 1.75)
                .add("peso", 85.0)
                .add("sexo", "MASCULINO").build();

        apiTestSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString())
                .put("/resources/membros/{membroId}/dados-fisicos", Map.of("membroId", membroId))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("altura", equalTo(payload.getJsonNumber("altura").bigDecimalValue().floatValue()))
                .body("peso", equalTo(payload.getJsonNumber("peso").bigDecimalValue().floatValue()))
                .body("sexo", equalTo(payload.getString("sexo")))
        ;

        apiTestSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/resources/membros/{membroId}/dados-fisicos", Map.of("membroId", membroId))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("altura", equalTo(payload.getJsonNumber("altura").bigDecimalValue().floatValue()))
                .body("peso", equalTo(payload.getJsonNumber("peso").bigDecimalValue().floatValue()))
                .body("sexo", equalTo(payload.getString("sexo")))
        ;

    }


}
