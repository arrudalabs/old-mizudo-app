package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.TestSupport;
import io.github.arrudalabs.mizudo.dto.Token;
import io.github.arrudalabs.mizudo.model.Papeis;
import io.github.arrudalabs.mizudo.resources.token.TokenResource;
import io.github.arrudalabs.mizudo.services.JwtTokenBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

@ApplicationScoped
public class ApiTestSupport {

    @Inject
    TestSupport testSupport;

    @Inject
    JwtTokenBuilder jwtTokenBuilder;

    public RequestSpecification newAuthenticatedRequest() {
        Token token = null;
        try {
            token = jwtTokenBuilder.gerarToken("admin", Set.of(Papeis.ADMINISTRADOR));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return given()
                .auth().oauth2(token.access_token())
                .when()
                .log().everything();
    }

    public RequestSpecification newNonAuthenticatedRequest() {
        return given()
                .when()
                .log().everything();
    }

    public void execute(Runnable runnable) {
        testSupport.execute(runnable);
    }

    public <R> R executeAndGet(Supplier<R> supplier) {
        return testSupport.executeAndGet(supplier);
    }


}
