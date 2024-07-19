package com.example.apiframework.client;

import com.example.apiframework.model.Auth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AuthClient {

    public AuthClient() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    public Response createToken(Auth auth) {
        return RestAssured.given()
                .contentType("application/json")
                .body(auth)
                .post("/auth");
    }
}
