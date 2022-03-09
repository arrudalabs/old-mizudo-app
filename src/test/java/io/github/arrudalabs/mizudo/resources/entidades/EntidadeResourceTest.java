package io.github.arrudalabs.mizudo.resources.entidades;

import io.github.arrudalabs.mizudo.model.Entidade;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.Map;

import static org.hamcrest.Matchers.*;


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntidadeResourceTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @BeforeEach
    @AfterEach
    void limparEntidades() {
        apiTestSupport.execute(Entidade::apagarEntidades);
    }

    @Test
    @Order(1)
    void deveRetornarUmaListaDeEntidadesVazia() {

        listaDeEntidades()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    private ValidatableResponse listaDeEntidades() {
        return apiTestSupport
                .newAuthenticatedRequest()
                .accept(ContentType.JSON)
                .get("/resources/entidades")
                .then();
    }

    @Test
    @Order(2)
    void deveRegistrarEntidadeValida() {

        apiTestSupport
                .newAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(Entidade.novaEntidade("JKA"))
                .post("/resources/entidades")
                .then()
                .statusCode(200)
                .body("id", not(emptyOrNullString()))
                .body("descricao", is("JKA"))
        ;

        listaDeEntidades()
                .log().everything()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].descricao", is("JKA"))
                .body("[0].id", not(emptyOrNullString()))

        ;

    }

    @Test
    @Order(3)
    void deveBuscarEntidadeValida() {
        var iska = apiTestSupport.executeAndGet(() ->{
            var entidade = Entidade.novaEntidade("ISKA");
            entidade.persist();
            return  entidade;
        });

        apiTestSupport
                .newAuthenticatedRequest()
                .accept(ContentType.JSON)
                .get("/resources/entidades/{id}", Map.of("id",iska.id))
                .then()
                .statusCode(200)
                .body("id", is(iska.id.intValue()))
                .body("descricao", is(iska.descricao))
        ;
    }



    @Test
    @Order(4)
    void deveEditarEntidadeValida() {
        var iska = apiTestSupport.executeAndGet(() ->{
            var entidade = Entidade.novaEntidade("ISKA");
            entidade.persist();
            return  entidade;
        });

        apiTestSupport
                .newAuthenticatedRequest()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(Entidade.novaEntidade("WKF"))
                .put("/resources/entidades/{id}", Map.of("id",iska.id))
                .then()
                .statusCode(200)
                .body("id", is(iska.id.intValue()))
                .body("descricao", is("WKF"))
        ;
    }

    @Test
    @Order(5)
    void deveRemoverEntidadeValida() {
        var iska = apiTestSupport.executeAndGet(() ->{
            var entidade = Entidade.novaEntidade("ISKA");
            entidade.persist();
            return  entidade;
        });

        apiTestSupport
                .newAuthenticatedRequest()
                .delete("/resources/entidades/{id}", Map.of("id",iska.id))
                .then()
                .statusCode(204)
        ;

        listaDeEntidades()
                .statusCode(200)
                .body("$", hasSize(0));

        apiTestSupport
                .newAuthenticatedRequest()
                .accept(ContentType.JSON)
                .get("/resources/entidades/{id}", Map.of("id",iska.id))
                .then()
                .statusCode(404)
        ;
    }

}