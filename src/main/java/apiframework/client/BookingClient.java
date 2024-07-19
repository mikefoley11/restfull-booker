package com.example.apiframework.client;

import com.example.apiframework.model.Booking;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BookingClient {

    public BookingClient() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    public Response createBooking(Booking booking) {
        return RestAssured.given()
                .contentType("application/json")
                .body(booking)
                .post("/booking");
    }

    public Response getBooking(int id) {
        return RestAssured.get("/booking/" + id);
    }

    public Response updateBooking(int id, Booking booking, String token) {
        return RestAssured.given()
                .contentType("application/json")
                .header("Cookie", "token=" + token)
                .body(booking)
                .put("/booking/" + id);
    }

    public Response deleteBooking(int id, String token) {
        return RestAssured.given()
                .header("Cookie", "token=" + token)
                .delete("/booking/" + id);
    }
}
