package com.cjbdevlabs.quarkus.dynamodb;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.contains;

@QuarkusTest
class CoffeeResourceTest {

    @Test
    void getAllReturnsAllCoffeeRoasts() {
        given()
                .when()
                .get("/coffee/roasts")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(3))
                .body("$", contains("Light Roast", "Medium Roast", "Dark Roast"));
    }
}