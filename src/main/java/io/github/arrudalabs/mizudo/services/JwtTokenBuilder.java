package io.github.arrudalabs.mizudo.services;

import io.github.arrudalabs.mizudo.dto.Token;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class JwtTokenBuilder {

    private final Long duracaoAccessTokenEmSegundos;
    private final Long duracaoRefreshTokenEmSegundos;
    private final String privateKey;
    private final String issuer;
    private final String keyId;

    public JwtTokenBuilder(
            @ConfigProperty(name = "jwt.access.token.duracao.segundos", defaultValue = "60") final Long duracaoAccessTokenEmSegundos,
            @ConfigProperty(name = "jwt.refresh.token.duracao.segundos", defaultValue = "1800") final Long duracaoRefreshTokenEmSegundos,
            @ConfigProperty(name = "jwt.private.key") final String privateKey,
            @ConfigProperty(name = "mp.jwt.verify.issuer") final String issuer
    ) {
        this.duracaoAccessTokenEmSegundos = duracaoAccessTokenEmSegundos;
        this.duracaoRefreshTokenEmSegundos = duracaoRefreshTokenEmSegundos;
        this.privateKey = privateKey;
        this.issuer = issuer;
        this.keyId = UUID.randomUUID().toString();
    }

    private String gerarAccessToken(String username,
                                    Set<String> papeis) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return gerarJwt(username, papeis, this.duracaoAccessTokenEmSegundos);
    }

    private String gerarRefreshToken(String username,
                                     Set<String> papeis) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return gerarJwt(username, papeis, this.duracaoRefreshTokenEmSegundos);
    }

    public Token gerarToken(String username,
                            Set<String> papeis) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new Token(gerarAccessToken(username, papeis), gerarRefreshToken(username, papeis));
    }


    private String gerarJwt(String username,
                            Set<String> papeis,
                            Long duracaoEmSegundos) throws NoSuchAlgorithmException, InvalidKeySpecException {

        PrivateKey privateKey = decodePrivateKey();

        return Jwt.preferredUserName(username)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(duracaoEmSegundos))
                .groups(papeis)
                .issuer(this.issuer)
                .audience(this.issuer)
                .sign(privateKey);
    }

    private PrivateKey decodePrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        var decodedKey = Base64.getDecoder().decode(this.privateKey.trim());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

}
