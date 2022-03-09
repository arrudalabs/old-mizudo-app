package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.ExameMedico;
import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.validation.DeveSerIdValido;
import io.github.arrudalabs.mizudo.validation.ValidationGroups;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.*;

@Path("/membros/{membroId}/exames-medicos")
public class ExamesMedicosDoMembroResource {

    @PUT
    @Transactional
    public Set<ExameMedico> definirExamesMedicosDoMembro(
            @PathParam("membroId")
            @DeveSerIdValido(
                    entityClass = Membro.class,
                    message = "O membro informado não é valido"
            ) Long membroId,
            @Valid @ConvertGroup(to = ValidationGroups.OnPut.class) final List<@NotNull ExameMedico> exameMedicos) {
        return Membro.buscarPorIdOptional(membroId)
                .map(membro -> {
                    membro.examesMedicos.clear();
                    membro.examesMedicos.addAll(exameMedicos);
                    return membro.examesMedicos;
                }).get();
    }

    @GET
    public Set<ExameMedico> listarExamesMedicosDoMembro(
            @PathParam("membroId")
            @DeveSerIdValido(
                    entityClass = Membro.class,
                    message = "O membro informado não é valido"
            ) Long membroId) {
        return Membro.buscarPorIdOptional(membroId)
                .filter(Objects::nonNull)
                .map(membro -> membro.examesMedicos)
                .orElseGet(HashSet::new);
    }
}
