package io.github.arrudalabs.mizudo.resources;

import io.restassured.specification.RequestSpecification;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

@ApplicationScoped
public class TestSupport {

    public RequestSpecification newAuthenticatedRequest() {
        return given()
                .when();
    }

    @Transactional
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Transactional
    public <R> R executeAndGet(Supplier<R> supplier) {
        return supplier.get();
    }

}
