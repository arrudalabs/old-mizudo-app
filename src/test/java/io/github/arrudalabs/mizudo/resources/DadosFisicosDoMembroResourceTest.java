package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.model.Membro;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.arrudalabs.mizudo.resources.TestSupport.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DadosFisicosDoMembroResourceTest {

    @BeforeEach
    @AfterEach
    public void removerMembros() {
        execute(() -> {
            Membro.deleteAll();
        });
    }

    @Transactional
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Test
    @DisplayName("deve adicionar os dados físicos do membro")
    public void test01() {

        AtomicLong membroId = new AtomicLong();
        execute(() -> {
            membroId.set(Membro.novoMembro(UUID.randomUUID().toString()).id);
        });

        JsonObject payload = Json.createObjectBuilder()
                .add("altura", 1.75)
                .add("peso", 85.0)
                .add("sexo", "MASCULINO").build();

        newRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString())
                .put("/resources/membros/{membroId}/dados-fisicos", Map.of("membroId", membroId.get()))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("altura", equalTo(payload.getJsonNumber("altura").bigDecimalValue().floatValue()))
                .body("peso", equalTo(payload.getJsonNumber("peso").bigDecimalValue().floatValue()))
                .body("sexo", equalTo(payload.getString("sexo")))
        ;

        newRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/resources/membros/{membroId}/dados-fisicos", Map.of("membroId", membroId.get()))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("altura", equalTo(payload.getJsonNumber("altura").bigDecimalValue().floatValue()))
                .body("peso", equalTo(payload.getJsonNumber("peso").bigDecimalValue().floatValue()))
                .body("sexo", equalTo(payload.getString("sexo")))
        ;

    }


}
