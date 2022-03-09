package io.github.arrudalabs.mizudo.resources.entidades;

import io.github.arrudalabs.mizudo.model.Entidade;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("entidades")
public class EntidadeResource {

    @POST
    @Transactional
    public Entidade novaEntidade(@Valid Entidade entidade) {
        entidade.persist();
        return entidade;
    }

    @GET
    public List<Entidade> listarEntidades() {
        return Entidade.listarEntidades().collect(Collectors.toList());
    }


    @GET
    @Path("{id}")
    public Entidade buscarEntidade(
            @PathParam("id") @NotNull Long id) {
        return Entidade.buscarPorId(id)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Entidade atualizarEntidade(
            @PathParam("id") @NotNull Long id,
            @Valid final Entidade entidade) {
        return Entidade.buscarPorId(id)
                .filter(Objects::nonNull)
                .map(entidadePersistida -> {
                    entidadePersistida.descricao = entidade.descricao;
                    return entidadePersistida;
                })
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response removerEntidade(
            @PathParam("id") @NotNull Long id) {
        return Entidade.buscarPorId(id)
                .filter(Objects::nonNull)
                .map(entidadePersistida -> {
                    entidadePersistida.apagarEntidade();
                    return Response.noContent().build();
                })
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }


}
