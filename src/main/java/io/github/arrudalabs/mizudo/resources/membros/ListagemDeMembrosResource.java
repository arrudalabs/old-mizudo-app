package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.validation.DeveSerMembroIdValido;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("membros")
public class ListagemDeMembrosResource {

    @GET
    public List<MembroRegistrado> listagemDeMembros() {

        return Membro.listarMembros()
                .map(MembroRegistrado::of)
                .collect(Collectors.toList());
    }

    @Path("{id}")
    @GET
    public MembroRegistrado buscarMembro(
            @PathParam("id")
            @NotNull Long id) {

        return Membro.buscarPorIdOptional(id)
                .filter(Objects::nonNull)
                .map(MembroRegistrado::of)
                .orElseThrow(()->new WebApplicationException(Response.Status.NOT_FOUND));

    }
}
