package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ListarMembrosResourceTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @BeforeEach
    @AfterEach
    void apagarMembrosCriadosNesteTeste() {
        apiTestSupport.execute(Membro::removerTodosMembros);
    }

    @Test
    @Order(1)
    void testListarMembrosCaso1() {

        var listaDeMembrosRetornada = solicitarListaDeMembros()
                .extract()
                .as(new TypeRef<List<Object>>() {
                });

        assertThat("deve retornar uma lista vazia caso não tenha membros persistidos no BD",
                listaDeMembrosRetornada,
                hasSize(0));

    }

    @Test
    @Order(2)
    void testListarMembrosCaso2() {

        Membro membro1 = apiTestSupport.executeAndGet(() ->
                Membro.novoMembro(UUID.randomUUID().toString()));

        List<Membro> listaDeMembrosRetornada =
                solicitarListaDeMembros()
                .extract()
                .as(new TypeRef<List<Membro>>(){});

        assertThat("deve retornar uma lista vazia com 1 membro persistido no BD",
                listaDeMembrosRetornada,
                hasSize(1));

        assertThat("membro retornado não tem o id esperado",
                listaDeMembrosRetornada.get(0).id,
                equalTo(membro1.id));

        assertThat("membro retornado não tem o nome esperado",
                listaDeMembrosRetornada.get(0).nome,
                equalTo(membro1.nome));

    }

    @Test
    @Order(3)
    public void testBuscarUmMembroQueExiste(){

        Membro membroPersistido = apiTestSupport.executeAndGet(() -> {
            return Membro.novoMembro(UUID.randomUUID().toString());
        });

        buscarMembro(membroPersistido.id)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(membroPersistido.id.intValue()))
                .body("nome", equalTo(membroPersistido.nome));


    }

    private ValidatableResponse buscarMembro(Object membroId) {
        return apiTestSupport
                .newAuthenticatedRequest()
                .accept(ContentType.JSON)
                .get("/resources/membros/{id}", membroId)
                .then();
    }

    @Test
    @Order(4)
    public void testBuscarUmMembroInvalido(){

       buscarMembro(99L)
               .statusCode(Response.Status.NOT_FOUND.getStatusCode());

       buscarMembro("rwerwer")
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }


    private ValidatableResponse solicitarListaDeMembros() {
        return apiTestSupport
                .newAuthenticatedRequest()
                .accept(ContentType.JSON)
                .get("/resources/membros")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

}
