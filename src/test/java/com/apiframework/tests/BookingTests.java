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

public class BookingTests {

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
    public void createBookingTest() {
        Booking booking = new Booking();
        booking.setFirstname("John");
        booking.setLastname("Doe");
        booking.setTotalprice(123);
        booking.setDepositpaid(true);
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2024-07-01");
        bookingDates.setCheckout("2024-07-10");
        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds("Breakfast");

        Response response = bookingClient.createBooking(booking);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void getBookingTest() {
        int bookingId = 1; // Example booking ID
        Response response = bookingClient.getBooking(bookingId);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void updateBookingTest() {
        int bookingId = 1; // Example booking ID
        Booking booking = new Booking();
        booking.setFirstname("Jane");
        booking.setLastname("Doe");
        booking.setTotalprice(456);
        booking.setDepositpaid(false);
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2024-07-15");
        bookingDates.setCheckout("2024-07-20");
        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds("Lunch");

        Response response = bookingClient.updateBooking(bookingId, booking, token);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void deleteBookingTest() {
        int bookingId = 1; // Example booking ID
        Response response = bookingClient.deleteBooking(bookingId, token);
        Assertions.assertEquals(201, response.getStatusCode());
    }
}
