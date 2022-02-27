package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.model.Membro;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/resources/membros")
@Transactional
public class NovoMembroResource {

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public MembroRegistrado novoMembro(@Valid NovoMembroResource.NovoMembro novoMembro) {
        return MembroRegistrado.of(novoMembro.criarNovoMembro());
    }

    public static class NovoMembro {

        @NotBlank
        public String nome;

        Membro criarNovoMembro() {
            Membro novoMembro = Membro.novoMembro(this.nome);
            return novoMembro;
        }

    }

    public static class MembroRegistrado {

        public Long id;
        public String nome;

        static MembroRegistrado of(Membro membro) {
            var response = new MembroRegistrado();
            response.id = membro.id;
            response.nome = membro.nome;
            return response;
        }

    }
}
