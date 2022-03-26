package io.github.arrudalabs.mizudo.resources.token;


import io.github.arrudalabs.mizudo.model.Papeis;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.services.GeradorDeHash;
import io.github.arrudalabs.mizudo.services.JwtTokenBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.Collectors;

@Path("/token")
public class TokenResource {

    private final JwtTokenBuilder jwtTokenBuilder;
    private GeradorDeHash geradorDeHash;

    @Inject
    public TokenResource(JwtTokenBuilder jwtTokenBuilder,
                         GeradorDeHash geradorDeHash) {
        this.jwtTokenBuilder = jwtTokenBuilder;
        this.geradorDeHash = geradorDeHash;
    }

    @POST
    public Token authenticate(@Valid Credenciais credenciais) {
        return Usuario
                .autentica(
                        credenciais.username(),
                        credenciais.senha(),
                        geradorDeHash)
                .map(this::criarToken)
                .orElseThrow(() -> new WebApplicationException(Response.Status.UNAUTHORIZED));
    }

    private Token criarToken(Usuario usuario) {
        return new Token(
                getAccess_token(usuario));

    }

    private String getAccess_token(Usuario usuario) {
        try {
            return jwtTokenBuilder.gerarToken(
                    usuario.username,
                    usuario.papeis
                            .stream()
                            .map(Papeis::name)
                            .collect(Collectors.toSet()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static record Credenciais(
            @NotBlank
            String username,
            @NotBlank
            String senha) {
    }

    public static record Token(String access_token) {
    }

}