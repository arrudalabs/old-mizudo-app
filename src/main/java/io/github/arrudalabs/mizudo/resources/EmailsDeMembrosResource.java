package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.validation.DeveSerMembroIdValido;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Path("/resources/membros/{membroId}/emails")
public class EmailsDeMembrosResource {

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public List<String> adicionarEmailAoMembro(
            @PathParam("membroId")
            @DeveSerMembroIdValido final Long membroId,
            @Valid List<@NotBlank @Email String> emails) {
        var membro = Membro.buscarPorId(membroId);
        membro.emails.clear();
        emails.forEach(membro.emails::add);
        return membro.emails;
    }

    @GET
    public List<String> adicionarEmailAoMembro(
            @PathParam("membroId")
            @DeveSerMembroIdValido final Long membroId) {
        var membro = Membro.buscarPorId(membroId);
        return Optional.ofNullable(membro.emails).orElseGet(LinkedList::new);
    }

}
