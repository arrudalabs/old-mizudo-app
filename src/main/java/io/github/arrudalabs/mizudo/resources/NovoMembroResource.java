package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.validation.NaoDeveConterMembroComMesmoNome;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/resources/membros")
public class NovoMembroResource {

    @POST
    @Consumes({
            MediaType.APPLICATION_JSON
    })
    @Produces({
            MediaType.APPLICATION_JSON
    })
    @Transactional
    public NovoMembroResponse adicionarMembro(@Valid NovoMembroRequest novoMembroRequest) {
        return NovoMembroResponse.of(novoMembroRequest.novoMembro());
    }


    public static class NovoMembroRequest {

        @NotBlank
        @NaoDeveConterMembroComMesmoNome
        public String nome;

        public Membro novoMembro() {
            Membro novoMembro = Membro.novoMembro(this.nome);
            novoMembro.persist();
            return novoMembro;
        }
    }

    public static class NovoMembroResponse {

        public static NovoMembroResponse of(Membro membro) {
            var novoMembroResponse = new NovoMembroResponse();
            novoMembroResponse.id = membro.id;
            novoMembroResponse.nome = membro.nome;
            return novoMembroResponse;
        }

        public Long id;
        public String nome;

    }
}
