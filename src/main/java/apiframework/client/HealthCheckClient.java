package com.apiframework.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HealthCheckClient {

    public HealthCheckClient() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    public Response healthCheck() {
        return RestAssured.get("/ping");
    }
}
