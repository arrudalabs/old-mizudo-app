package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class NovoMembroResourceTest {

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
    @DisplayName("deve adicionar novo membro")
    @Order(1)
    public void teste01() {
        apiTestSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("nome", "Maximillian"))
                .post("/resources/membros")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("id", not(blankOrNullString()))
                .body("nome", equalTo("Maximillian"));
    }

    @ParameterizedTest(name = "{index} {0}")
    @DisplayName("não deve adicionar novo membro sem informar um nome válido")
    @Order(3)
    @MethodSource("teste03Args")
    public void teste03(String caso, String nome) {

        apiTestSupport.newAuthenticatedRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Objects.isNull(nome) ?
                        Json.createObjectBuilder().addNull("nome").build():
                        Json.createObjectBuilder().add("nome", nome).build())
                .post("/resources/membros")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    public static Stream<Arguments> teste03Args() {
        return Stream.of(
                Arguments.arguments(
                        "caso o nome venha em branco", " "
                ),
                Arguments.arguments(
                        "caso o nome venha em vazio", ""
                ),
                Arguments.arguments(
                        "caso o nome venha nulo", null
                )
        );
    }
}
