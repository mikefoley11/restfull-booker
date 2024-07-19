package com.apiframework.client;

import com.apiframework.model.Booking;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BookingClient {
    private static final Logger logger = LoggerFactory.getLogger(BookingClient.class);

    public BookingClient() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    public Response createBooking(Booking booking) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(booking)
                .post("/booking");
        
        if (response.getStatusCode() == 200) {
            logger.info("Created Booking: {}", response.getBody().asString());
        }
        
        return response;
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

    public Response getAllBookingIds() {
        Response response = RestAssured.get("/booking");
        logger.info("Retrieved All Booking IDs: {}", response.getBody().asString());
        return response;
    }
}
