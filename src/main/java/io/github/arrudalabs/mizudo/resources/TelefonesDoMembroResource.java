package io.github.arrudalabs.mizudo.resources;


import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Telefone;
import io.github.arrudalabs.mizudo.validation.DeveSerMembroIdValido;
import io.github.arrudalabs.mizudo.validation.ValidationGroups;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/resources/membros/{membroId}/telefones")
public class TelefonesDoMembroResource {

    @PUT
    @Transactional
    public List<Telefone> setTelefonesDoMembro(
            @PathParam("membroId")
            @DeveSerMembroIdValido final Long membroId,
            @Valid
            @ConvertGroup(to = ValidationGroups.OnPut.class)
                    List<@NotNull Telefone> telefones
    ) {
        var membro=Membro.buscarPorId(membroId);
        membro.telefones.clear();
        membro.telefones.addAll(telefones);
        return membro.telefones;
    }

    @GET
    public List<Telefone> getTelefonesDoMembro(
            @PathParam("membroId")
            @DeveSerMembroIdValido final Long membroId) {
        return Membro.buscarPorId(membroId).telefones;
    }

}
