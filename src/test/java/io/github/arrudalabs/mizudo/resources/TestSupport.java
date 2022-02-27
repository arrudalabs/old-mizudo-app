package io.github.arrudalabs.mizudo.resources;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TestSupport {

    public static RequestSpecification newRequest() {
        return given()
                .when();
    }

}
