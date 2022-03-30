package io.github.arrudalabs.mizudo.resources.token;


import io.github.arrudalabs.mizudo.model.Papeis;
import io.github.arrudalabs.mizudo.model.Usuario;
import io.github.arrudalabs.mizudo.services.GeradorDeHash;
import io.github.arrudalabs.mizudo.services.JwtTokenBuilder;
import io.github.arrudalabs.mizudo.dto.Credenciais;
import io.github.arrudalabs.mizudo.dto.Token;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/token")
@RequestScoped
public class TokenResource {

    private final String adminSecret;
    private final JwtTokenBuilder jwtTokenBuilder;
    private final GeradorDeHash geradorDeHash;
    private final Optional<Usuario> adminUser;

    public TokenResource(@ConfigProperty(name = "admin.secret") String adminSecret,
                         JwtTokenBuilder jwtTokenBuilder,
                         GeradorDeHash geradorDeHash) {
        this.adminSecret = adminSecret;
        this.jwtTokenBuilder = jwtTokenBuilder;
        this.geradorDeHash = geradorDeHash;
        this.adminUser = Optional.of(Usuario.novoUsuario("admin", Papeis.Papel.values()));

    }

    @POST
    public Token autentique(@Valid Credenciais credenciais) {
        return autenticarAdmin(credenciais)
                .or(() -> autenticarUsuario(credenciais))
                .map(this::criarToken)
                .orElseThrow(() -> new WebApplicationException(Response.Status.UNAUTHORIZED));
    }

    private Optional<Usuario> autenticarAdmin(Credenciais credenciais) {
        return adminUser
                .filter(usuario -> Objects.equals(usuario.username, credenciais.username()))
                .filter(username -> this.adminSecret.equals(credenciais.senha()));
    }

    private Optional<Usuario> autenticarUsuario(Credenciais credenciais) {
        return Usuario
                .autentica(
                        credenciais.username(),
                        credenciais.senha(),
                        geradorDeHash);
    }

    private Token criarToken(Usuario usuario) {
        try {
            return jwtTokenBuilder.
                    gerarToken(usuario.username, usuario.papeis.stream().map(Papeis.Papel::name).collect(Collectors.toSet()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }




}