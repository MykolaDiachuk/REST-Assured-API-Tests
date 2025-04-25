package epam.api.services;

import epam.api.entities.BookingDTO;
import epam.api.entities.BookingResponseDTO;
import epam.api.utils.HTTPClient;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingResponseDTO createBooking(BookingDTO bookingDTO) {
        Response response = HTTPClient.POST("/booking", bookingDTO);

        int bookingId = response.jsonPath().getInt("bookingid");
        logger.info("Booking created with ID: {}", bookingId);

        return new BookingResponseDTO(bookingId, bookingDTO);
    }

    public Response getBookingById(int bookingId) {
        logger.info("GET booking with ID: {}", bookingId);
        return HTTPClient.GET("/booking/" + bookingId);
    }

    public Response deleteBooking(int bookingId) {
        logger.info("DELETE booking with ID: {}", bookingId);
       return HTTPClient.DELETE("/booking/" + bookingId);
    }

    public Response updateBookingWithPatch(int bookingId, BookingDTO partialBookingDTO) {
        return HTTPClient.PATCH("/booking/" + bookingId, partialBookingDTO);
    }

    public Response updateBookingWithPut(int bookingId, BookingDTO fullBookingDTO) {
        return HTTPClient.PUT("/booking/" + bookingId, fullBookingDTO);
    }

}
