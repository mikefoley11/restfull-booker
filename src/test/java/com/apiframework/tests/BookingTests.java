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

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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

    private Booking createTestBooking(String firstname, String lastname, int totalprice, boolean depositpaid, String additionalneeds) {
        Booking booking = new Booking();
        booking.setFirstname(firstname);
        booking.setLastname(lastname);
        booking.setTotalprice(totalprice);
        booking.setDepositpaid(depositpaid);

        BookingDates bookingDates = new BookingDates();
        Random random = new Random();
        LocalDate checkin = LocalDate.now().plusDays(random.nextInt(10) + 1);
        LocalDate checkout = checkin.plusDays(random.nextInt(5) + 1);
        bookingDates.setCheckin(checkin.toString());
        bookingDates.setCheckout(checkout.toString());
        booking.setBookingdates(bookingDates);

        booking.setAdditionalneeds(additionalneeds);
        return booking;
    }

    @Test
    public void createAndLogBookingsTest() throws IOException {
        StringBuilder logBuilder = new StringBuilder();
        
        // Create Test1 booking
        Booking test1 = createTestBooking("Test1", "User1", 500, true, "Lunch");
        Response test1Response = bookingClient.createBooking(test1);
        Assertions.assertEquals(200, test1Response.getStatusCode());
        logger.info("Created Test1 Booking: {}", test1Response.getBody().asString());
        logBuilder.append("<p>Created Test1 Booking: ").append(test1Response.getBody().asString()).append("</p>");

        // Create Test2 booking
        Booking test2 = createTestBooking("Test2", "User2", 1000, false, "");
        Response test2Response = bookingClient.createBooking(test2);
        Assertions.assertEquals(200, test2Response.getStatusCode());
        logger.info("Created Test2 Booking: {}", test2Response.getBody().asString());
        logBuilder.append("<p>Created Test2 Booking: ").append(test2Response.getBody().asString()).append("</p>");

        // Create and log another booking
        Booking test3 = createTestBooking("Test3", "User3", 500, true, "Breakfast");
        Response test3Response = bookingClient.createBooking(test3);
        Assertions.assertEquals(200, test3Response.getStatusCode());
        logger.info("Created Test3 Booking: {}", test3Response.getBody().asString());
        logBuilder.append("<p>Created Test3 Booking: ").append(test3Response.getBody().asString()).append("</p>");

        // Log all available booking IDs
        Response allBookingIdsResponse = bookingClient.getAllBookingIds();
        Assertions.assertEquals(200, allBookingIdsResponse.getStatusCode());
        logger.info("Retrieved All Booking IDs: {}", allBookingIdsResponse.getBody().asString());
        logBuilder.append("<p>Retrieved All Booking IDs: ").append(allBookingIdsResponse.getBody().asString()).append("</p>");

        // Update Test1 total price to 1000
        int test1Id = test1Response.jsonPath().getInt("bookingid");
        test1.setTotalprice(1000);
        Response updateTest1Response = bookingClient.updateBooking(test1Id, test1, token);
        Assertions.assertEquals(200, updateTest1Response.getStatusCode());
        logger.info("Updated Test1 Booking: {}", updateTest1Response.getBody().asString());
        logBuilder.append("<p>Updated Test1 Booking: ").append(updateTest1Response.getBody().asString()).append("</p>");

        // Update Test2 total price to 1500
        int test2Id = test2Response.jsonPath().getInt("bookingid");
        test2.setTotalprice(1500);
        Response updateTest2Response = bookingClient.updateBooking(test2Id, test2, token);
        Assertions.assertEquals(200, updateTest2Response.getStatusCode());
        logger.info("Updated Test2 Booking: {}", updateTest2Response.getBody().asString());
        logBuilder.append("<p>Updated Test2 Booking: ").append(updateTest2Response.getBody().asString()).append("</p>");

        // Delete one booking (Test3) and log status
        int test3Id = test3Response.jsonPath().getInt("bookingid");
        Response deleteTest3Response = bookingClient.deleteBooking(test3Id, token);
        Assertions.assertEquals(201, deleteTest3Response.getStatusCode());
        logger.info("Deleted Test3 Booking: ID {}", test3Id);
        logBuilder.append("<p>Deleted Test3 Booking: ID ").append(test3Id).append("</p>");

        // Save log to HTML file
        FileWriter fileWriter = new FileWriter("logs/test_report.html");
        fileWriter.write("<html><body>");
        fileWriter.write(logBuilder.toString());
        fileWriter.write("</body></html>");
        fileWriter.close();
    }
}
