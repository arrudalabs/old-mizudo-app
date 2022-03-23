package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.DadosGerais;
import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.validation.DeveSerIdValido;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/membros/{membroId}/dados-gerais")
public class DadosGeraisDoMembroResource {

    @PUT
    @Transactional
    public DadosGerais setDadosGerais(@PathParam("membroId")
                                      @DeveSerIdValido(
                                              entityClass = Membro.class,
                                              message = "O membro informado não é valido"
                                      ) final Long membroId,
                                      @Valid final DadosGerais dadosGerais) {
        Membro.buscarPorId(membroId)
                .ifPresent(membro -> membro.dadosGerais = dadosGerais);
        return dadosGerais;
    }


    @GET
    public DadosGerais getDadosGerais(@PathParam("membroId")
                                          @DeveSerIdValido(
                                                  entityClass = Membro.class,
                                                  message = "Membro inválido"
                                          ) final Long membroId) {
        return Membro.buscarPorId(membroId)
                .map(membro -> membro.dadosGerais)
                .orElseGet(DadosGerais::new);
    }
}
