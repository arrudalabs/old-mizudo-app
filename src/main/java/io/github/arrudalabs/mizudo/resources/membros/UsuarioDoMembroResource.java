package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.dto.NovoUsuario;
import io.github.arrudalabs.mizudo.dto.UsuarioRegistrado;
import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.services.GeradorDeHash;
import io.github.arrudalabs.mizudo.validation.DeveSerIdValido;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/membros/{membroId}/user")
@RequestScoped
public class UsuarioDoMembroResource {

    @Inject
    GeradorDeHash geradorDeHash;

    @PUT
    @Transactional
    public UsuarioRegistrado definirUsuario(
            @DeveSerIdValido(
                    entityClass = Membro.class,
                    message = "Membro inválido"
            )
            @PathParam("membroId") final Long membroId,
            @Valid final NovoUsuario novoUsuario) {

        return new UsuarioRegistrado(
                Usuario.definirUsuarioParaMembroInformado(
                        Membro.findById(membroId),
                        novoUsuario.username(),
                        novoUsuario.senha(),
                        geradorDeHash));
    }

}
