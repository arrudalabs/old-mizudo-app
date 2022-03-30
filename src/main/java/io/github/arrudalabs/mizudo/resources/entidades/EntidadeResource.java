package io.github.arrudalabs.mizudo.resources.entidades;

import io.github.arrudalabs.mizudo.dto.NovaEntidade;
import io.github.arrudalabs.mizudo.model.Entidade;
import io.github.arrudalabs.mizudo.model.Papeis;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("entidades")
@RequestScoped
public class EntidadeResource {

    @POST
    @Transactional
    @RolesAllowed({Papeis.ADMINISTRADOR, Papeis.COORDENADOR})
    public Entidade novaEntidade(@Valid NovaEntidade novaEntidade) {
        Entidade entidade = novaEntidade.criarEntidade();
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
    @RolesAllowed({Papeis.ADMINISTRADOR, Papeis.COORDENADOR})
    public Entidade atualizarEntidade(
            @PathParam("id") @NotNull Long id,
            @Valid final Entidade entidade) {
        return Entidade.buscarPorId(id)
                .stream().peek(entidadePersistida -> entidadePersistida.descricao = entidade.descricao)
                .findFirst()
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @RolesAllowed({Papeis.ADMINISTRADOR, Papeis.COORDENADOR})
    public Response removerEntidade(
            @PathParam("id") @NotNull Long id) {
        return Entidade.buscarPorId(id)
                .stream().peek(Entidade::apagarEntidade)
                .map(e -> Response.noContent().build())
                .findFirst()
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

}
