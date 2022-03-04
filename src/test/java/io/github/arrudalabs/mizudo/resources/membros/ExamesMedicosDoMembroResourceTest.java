package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.ExameMedico;
import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.resources.ApiTestSupport;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ExamesMedicosDoMembroResourceTest {

    @Inject
    ApiTestSupport apiTestSupport;

    @BeforeEach
    @AfterEach
    public void apagarOsMembrosCriadosNesteTeste() {
        apiTestSupport.execute(Membro::removerTodosMembros);
    }

    @Test
    @DisplayName("definir exames médicos a um membro válido")
    public void definirExamesMedicosAUmMembroValido() {
        Long membroId = getMembroId();

        List<ExameMedico> examesMedicosRegistrados = listarExamesMedicos(membroId);

        assertThat(
                "deve retornar uma lista vazia caso o membro não tenha nenhum exame médico registrado",
                examesMedicosRegistrados,
                hasSize(0)
        );

        ExameMedico exameMedico01 = ExameMedico
                .novoExameMedico(LocalDate.now().minusDays(3), "A".repeat(100));

        ExameMedico exameMedico02 = ExameMedico
                .novoExameMedico(LocalDate.now().minusDays(2), "B".repeat(100));

        ExameMedico exameMedico03 = ExameMedico
                .novoExameMedico(LocalDate.now().minusDays(1), "C".repeat(100));

        examesMedicosRegistrados = definirExamesMedicos(membroId, exameMedico01, exameMedico02, exameMedico03)
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(new TypeRef<List<ExameMedico>>() {
                });

        assertThat(
                examesMedicosRegistrados,
                hasItems(exameMedico01, exameMedico02, exameMedico03)
        );

        examesMedicosRegistrados = listarExamesMedicos(membroId);

        assertThat(
                "não foi persistido os exames médicos no BD",
                examesMedicosRegistrados,
                hasItems(exameMedico01, exameMedico02, exameMedico03)
        );

    }

    private List<ExameMedico> listarExamesMedicos(Long membroId) {
        return apiTestSupport
                .newAuthenticatedRequest()
                .log().all()
                .accept(ContentType.JSON)
                .get("/resources/membros/{membroId}/exames-medicos", Map.of("membroId", membroId))
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(new TypeRef<List<ExameMedico>>() {
                });
    }

    private ValidatableResponse definirExamesMedicos(Long membroId, ExameMedico... exameMedicos) {

        var jsonArray = Json
                .createArrayBuilder();

        Arrays.stream(exameMedicos)
                .map(exameMedico -> {
                    return Optional.ofNullable(exameMedico)
                            .filter(Objects::nonNull)
                            .map(em -> {
                                JsonObjectBuilder json = Json.createObjectBuilder();
                                if (Objects.isNull(em.data)) {
                                    json.addNull("data");
                                } else {
                                    json.add("data", em.data.toString());
                                }
                                if (Objects.isNull(em.obs)) {
                                    json.addNull("obs");
                                } else {
                                    json.add("obs", em.obs);
                                }
                                return json.build();
                            })
                            .orElseGet(() -> Json.createObjectBuilder().build());
                }).forEach(jsonArray::add);

        return apiTestSupport
                .newAuthenticatedRequest()
                .log().all()
                .contentType(ContentType.JSON)
                .body(jsonArray.build().toString())
                .put("/resources/membros/{membroId}/exames-medicos", Map.of("membroId", membroId))
                .then()
                .log().all();
    }

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("naoAceitarExamesMedicosInvalidosAoMembro_args")
    public void naoAceitarExamesMedicosInvalidosAoMembro(
            String cenarioDoTeste,
            ExameMedico exameMedico
    ) {
        Long membroId = getMembroId();
        definirExamesMedicos(membroId,exameMedico )
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    private Long getMembroId() {
        return apiTestSupport.executeAndGet(() -> Membro.novoMembro(UUID.randomUUID().toString()).id);
    }

    static Stream<Arguments> naoAceitarExamesMedicosInvalidosAoMembro_args(){
        return Stream.of(
                Arguments.arguments(
                        "não deve aceitar exame médico sem data",
                        ExameMedico.novoExameMedico(null,"C".repeat(10))
                )
        );
    }


}
