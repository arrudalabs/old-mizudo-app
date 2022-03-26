package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.TestSupport;
import io.restassured.specification.RequestSpecification;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

@ApplicationScoped
public class ApiTestSupport {

    @Inject
    TestSupport testSupport;


    public RequestSpecification newAuthenticatedRequest() {
        return given()
                .when();
    }

    public RequestSpecification newNonAuthenticatedRequest() {
        return given()
                .when();
    }

    public void execute(Runnable runnable) {
        testSupport.execute(runnable);
    }

    public <R> R executeAndGet(Supplier<R> supplier) {
        return testSupport.executeAndGet(supplier);
    }


}
