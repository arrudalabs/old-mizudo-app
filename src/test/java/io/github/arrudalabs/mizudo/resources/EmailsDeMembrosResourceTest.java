package io.github.arrudalabs.mizudo.resources;


import io.github.arrudalabs.mizudo.model.Membro;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static io.github.arrudalabs.mizudo.resources.TestSupport.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class EmailsDeMembrosResourceTest {

    @Transactional
    public <R> R execute(Supplier<R> supplier) {
        return supplier.get();
    }

    @BeforeEach
    @AfterEach
    public void limparMembros() {
        execute(() -> {
            Membro.removerTodosMembros();
            return null;
        });
    }

    @Test
    @DisplayName("adicionar um email válido a um membro")
    public void test01() {

        var membroValido = execute(() -> {
            return Membro.novoMembro(UUID.randomUUID().toString());
        });

        var emailEsperado1 = "dearrudam1@gmail.com";
        var emailEsperado2 = "dearrudam2@gmail.com";
        var emailEsperado3 = "dearrudam3@gmail.com";

        Long membroId = membroValido.id;
        setEmailsDoMembro(membroId, emailEsperado1)
                .body("$", hasItems(emailEsperado1));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(1));

        setEmailsDoMembro(membroId,emailEsperado1,emailEsperado2)
                .body("$", hasItems(emailEsperado1, emailEsperado2));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(2));

        setEmailsDoMembro(membroId,emailEsperado1,emailEsperado2,emailEsperado3)
                .body("$", hasItems(emailEsperado1, emailEsperado2, emailEsperado3));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(3));

        setEmailsDoMembro(membroId)
                .body("$", hasSize(0));

        listarEmailsDoMembro(membroId)
                .body("$", hasSize(0));

    }

    private ValidatableResponse setEmailsDoMembro(Long membroId, String... emails) {
        return newRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(emails)
                .put("/resources/membros/{id}/emails", Map.of("id", membroId))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                ;
    }

    private ValidatableResponse listarEmailsDoMembro(Long membroId) {
        return newRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/resources/membros/{id}/emails", Map.of("id", membroId))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                ;
    }

}
