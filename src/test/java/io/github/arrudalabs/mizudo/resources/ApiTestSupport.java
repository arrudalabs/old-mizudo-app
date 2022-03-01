package io.github.arrudalabs.mizudo.resources;

import io.github.arrudalabs.mizudo.TestSupport;
import io.restassured.specification.RequestSpecification;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

@ApplicationScoped
public class ApiTestSupport extends TestSupport {

    public RequestSpecification newAuthenticatedRequest() {
        return given()
                .when();
    }


}
