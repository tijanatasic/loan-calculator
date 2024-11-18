package com.calculator.app.integration.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-test")
public class BaseIT {

    @BeforeAll
    public static void initRestAssured() {
        RestAssured.baseURI = RestAssured.DEFAULT_URI;
    }

}
