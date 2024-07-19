package com.apiframework.tests;

import com.apiframework.client.AuthClient;
import com.apiframework.model.Auth;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthTests {

    private AuthClient authClient;

    @BeforeEach
    public void setup() {
        authClient = new AuthClient();
    }

    @Test
    public void createTokenTest() {
        Auth auth = new Auth();
        auth.setUsername("admin");
        auth.setPassword("password123");
        Response response = authClient.createToken(auth);
        Assertions.assertEquals(200, response.getStatusCode());
        String token = response.jsonPath().getString("token");
        Assertions.assertNotNull(token);
    }
}
