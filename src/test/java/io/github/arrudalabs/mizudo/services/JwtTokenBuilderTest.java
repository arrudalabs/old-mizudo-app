package io.github.arrudalabs.mizudo.services;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

@QuarkusTest
class JwtTokenBuilderTest {

    @Inject
    JwtTokenBuilder jwtTokenBuilder;

    @Test
    void deveGerarUmTokenValido() throws NoSuchAlgorithmException, InvalidKeySpecException {

        System.out.println(jwtTokenBuilder.gerarToken("admin", Set.of("ADMIN", "USER")));

    }


}