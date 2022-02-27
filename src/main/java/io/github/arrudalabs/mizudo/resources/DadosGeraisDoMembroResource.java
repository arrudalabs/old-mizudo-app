package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.model.DadosGerais;
import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.validation.DeveSerMembroIdValido;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/resources/membros/{membroId}/dados-gerais")
public class DadosGeraisDoMembroResource {

    @PUT
    @Transactional
    public DadosGerais setDadosGerais(@PathParam("membroId")
                            @DeveSerMembroIdValido final Long membroId,
                           @Valid final DadosGerais dadosGerais) {
        Membro membro = Membro.buscarPorId(membroId);
        membro.dadosGerais = dadosGerais;
        return dadosGerais;
    }


    @GET
    public DadosGerais getDadosGerais(@PathParam("membroId") final Long membroId) {
        Membro membro = Membro.buscarPorId(membroId);
        if (membro == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return Optional.ofNullable(membro.dadosGerais).orElseGet(DadosGerais::new);
    }
}
