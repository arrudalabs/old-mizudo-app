package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.DadosFisicos;
import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.validation.DeveSerMembroIdValido;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Optional;

@Path("/membros/{membroId}/dados-fisicos")
public class DadosFisicosDoMembroResource {

    @PUT
    @Transactional
    public DadosFisicos setDadosFisicos(@PathParam("membroId")
                                        @DeveSerMembroIdValido Long membroId,
                                        @Valid DadosFisicos dadosFisicos) {
        Membro membro = Membro.findById(membroId);
        membro.dadosFisicos = dadosFisicos;
        return dadosFisicos;
    }

    @GET
    public DadosFisicos getDadosFisicos(@PathParam("membroId")
                                        @DeveSerMembroIdValido Long membroId){
        Membro membro = Membro.findById(membroId);
        return Optional.ofNullable(membro.dadosFisicos).orElseGet(DadosFisicos::new);
    }

}
