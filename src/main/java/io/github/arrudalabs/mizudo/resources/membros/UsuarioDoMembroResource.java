package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.validation.DeveSerIdValido;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/membros/{membroId}/user")
public class UsuarioDoMembroResource {

    @PUT
    public UsuarioRegistrado definirUsuario(
            @DeveSerIdValido(
                    entityClass = Membro.class,
                    message = "Membro inválido"
            )
            @PathParam("membroId") final Long membroId,
            @Valid final NovoUsuario novoUsuario) {

        return UsuarioRegistrado.of(novoUsuario.definirUsuario(membroId));
    }

    public static class NovoUsuario {

        @NotBlank
        public String username;
        @NotBlank
        public String senha;
        @NotBlank
        public String confirmacaoSenha;

        public Usuario definirUsuario(Long membroId) {
            var usuario = new Usuario();
            usuario.username = this.username;
            //TODO implementar
            return usuario;
        }
    }

    public static class UsuarioRegistrado {

        public static UsuarioRegistrado of(Usuario usuario) {
            var usuarioRegistrado = new UsuarioRegistrado();
            usuarioRegistrado.username = usuario.username;
            return usuarioRegistrado;
        }

        public String username;
        public String senha = "*****";
    }

}
