package io.github.arrudalabs.mizudo.services;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class JwtTokenBuilder {

    private final Long duracaoEmMinutos;
    private final String privateKey;
    private final String issuer;
    private final String keyId;

    public JwtTokenBuilder(
            @ConfigProperty(name = "jwt.token.duracao.minutos", defaultValue = "300") final Long duracaoEmMinutos,
            @ConfigProperty(name = "jwt.private.key") final String privateKey,
            @ConfigProperty(name = "jwt.issuer") final String issuer
    ) {
        this.duracaoEmMinutos = duracaoEmMinutos;
        this.privateKey = privateKey;
        this.issuer = issuer;
        this.keyId = UUID.randomUUID().toString();
    }

    public String gerarToken(String username,
                             Set<String> papeis) throws NoSuchAlgorithmException, InvalidKeySpecException {

        long currentTimeInSecs = System.currentTimeMillis() / 1000;
        var claimsBuilder = Jwt.claims();
        PrivateKey privateKey = decodePrivateKey();

        claimsBuilder.issuer(this.issuer);
        claimsBuilder.subject(username);
        claimsBuilder.issuedAt(currentTimeInSecs);
        claimsBuilder.expiresAt(tempoDeExpiracao(currentTimeInSecs));
        claimsBuilder.groups(papeis);

        return claimsBuilder.jws().keyId(this.keyId).sign(privateKey);
    }

    private PrivateKey decodePrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        var decodedKey = Base64.getDecoder().decode(this.privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public long tempoDeExpiracao(long currentTimeInSecs) {
        return currentTimeInSecs + (this.duracaoEmMinutos * 60);
    }

}
