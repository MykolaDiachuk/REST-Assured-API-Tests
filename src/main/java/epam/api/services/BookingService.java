package epam.api.services;

import epam.api.entities.BookingDTO;
import epam.api.entities.BookingResponseDTO;
import epam.api.utils.BookingDTOGenerator;
import epam.api.utils.HTTPClient;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingResponseDTO createRandomBooking() {
        logger.info("Generate random booking and send POST request");
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();
        Response response = HTTPClient.POST("/booking", bookingDTO);

        int bookingId = response.jsonPath().getInt("bookingid");
        logger.info("Booking created with ID: {}", bookingId);

        return new BookingResponseDTO(bookingId, bookingDTO);
    }

    public BookingResponseDTO getBookingById(int bookingId) {
        logger.info("GET booking with ID: {}", bookingId);
        Response response = HTTPClient.GET("/booking/" + bookingId);
        response.then().statusCode(200);
        BookingDTO dto = response.as(BookingDTO.class);
        return new BookingResponseDTO(bookingId, dto);
    }

    public void deleteBooking(int bookingId) {
        logger.info("DELETE booking with ID: {}", bookingId);
        HTTPClient.DELETE("/booking/" + bookingId);
    }

    public void assertBookingDeleted(int bookingId) {
        logger.info("Verify booking with ID {} no longer exists", bookingId);
        HTTPClient.GET("/booking/" + bookingId).then().statusCode(404);
    }
}
