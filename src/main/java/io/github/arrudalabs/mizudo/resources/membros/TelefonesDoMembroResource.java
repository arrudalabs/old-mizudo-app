package io.github.arrudalabs.mizudo.resources.membros;


import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Telefone;
import io.github.arrudalabs.mizudo.validation.DeveSerIdValido;
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

@Path("/membros/{membroId}/telefones")
public class TelefonesDoMembroResource {

    @PUT
    @Transactional
    public List<Telefone> setTelefonesDoMembro(
            @PathParam("membroId")
            @DeveSerIdValido(
                    entityClass = Membro.class,
                    message = "O membro informado não é valido"
            )  final Long membroId,
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
            @DeveSerIdValido(
                    entityClass = Membro.class,
                    message = "O membro informado não é valido"
            )  final Long membroId) {
        return Membro.buscarPorId(membroId).telefones;
    }

}
