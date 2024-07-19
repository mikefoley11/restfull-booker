package com.apiframework.tests;

import com.apiframework.client.BookingClient;
import com.apiframework.client.AuthClient;
import com.apiframework.model.Booking;
import com.apiframework.model.BookingDates;
import com.apiframework.model.Auth;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingTests {
    private static final Logger logger = LoggerFactory.getLogger(BookingTests.class);
    private BookingClient bookingClient;
    private AuthClient authClient;
    private String token;

    @BeforeEach
    public void setup() {
        bookingClient = new BookingClient();
        authClient = new AuthClient();
        Auth auth = new Auth();
        auth.setUsername("admin");
        auth.setPassword("password123");
        Response authResponse = authClient.createToken(auth);
        token = authResponse.jsonPath().getString("token");
    }

    @Test
    public void createAndLogBookingsTest() {
        // Create and log 3 new bookings
        for (int i = 0; i < 3; i++) {
            Booking booking = new Booking();
            booking.setFirstname("John" + i);
            booking.setLastname("Doe" + i);
            booking.setTotalprice(123 + i);
            booking.setDepositpaid(true);
            BookingDates bookingDates = new BookingDates();
            bookingDates.setCheckin("2024-07-0" + (i + 1));
            bookingDates.setCheckout("2024-07-1" + (i + 1));
            booking.setBookingdates(bookingDates);
            booking.setAdditionalneeds("Breakfast");

            Response response = bookingClient.createBooking(booking);
            Assertions.assertEquals(200, response.getStatusCode());
            logger.info("Created Booking {}: {}", i + 1, response.getBody().asString());
        }

        // Log all available booking IDs
        Response allBookingIdsResponse = bookingClient.getAllBookingIds();
        Assertions.assertEquals(200, allBookingIdsResponse.getStatusCode());
    }
}
