package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.services.GeradorDeHash;
import io.github.arrudalabs.mizudo.validation.DeveSerIdValido;
import io.github.arrudalabs.mizudo.validation.SuportaValidacao;
import io.github.arrudalabs.mizudo.validation.Validacao;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Objects;

@Path("/membros/{membroId}/user")
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

        return new UsuarioRegistrado(Usuario.definirUsuario(Membro.findById(membroId), novoUsuario.username, novoUsuario.senha, geradorDeHash));
    }

    @SuportaValidacao(
            message = "senhas não conferem",
            classeValidadora = NovoUsuario.NovoUsuarioValidation.class
    )
    public record NovoUsuario(@NotBlank
                              String username,
                              @NotBlank
                              String senha,
                              @NotBlank
                              String confirmacaoSenha) {
        public static class NovoUsuarioValidation implements Validacao {
            @Override
            public boolean estahValido(Object object) {
                if (object instanceof NovoUsuario novoUsuario) {
                    return Objects.nonNull(novoUsuario)
                            && Objects.nonNull(novoUsuario.senha)
                            && novoUsuario.senha.equals(novoUsuario.confirmacaoSenha);
                }
                return false;
            }
        }
    }

    public record UsuarioRegistrado(String username, String senha) {

        public UsuarioRegistrado(Usuario usuario) {
            this(usuario.username, "*****");
        }
    }

}
