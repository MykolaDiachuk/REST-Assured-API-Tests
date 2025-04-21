package epam.api;

import epam.api.entities.BookingResponseDTO;
import epam.api.services.BookingService;
import epam.api.utils.config.ConfigReader;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BookingTests {

    private static final BookingService bookingService = new BookingService();

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.getProperty("base.url");
    }

    @Test()
    public void verifyBookingCreatedCorrectlyTest() {
        BookingResponseDTO expected = bookingService.createRandomBooking();
        BookingResponseDTO actual = bookingService.getBookingById(expected.getBookingId());

        Assertions.assertThat(actual).isEqualTo(expected)
                .describedAs("Verify booking data created correctly");
    }

    @Test()
    public void verifyBookingRetrievedCorrectlyTest() {
        BookingResponseDTO expected = bookingService.createRandomBooking();
        BookingResponseDTO actual = bookingService.getBookingById(expected.getBookingId());

        Assertions.assertThat(actual.getBooking()).isEqualTo(expected.getBooking())
                .describedAs("Verify booking data got correctly");
    }

    @Test()
    public void verifyBookingDeletedSuccessfullyTest() {
        BookingResponseDTO booking = bookingService.createRandomBooking();
        bookingService.deleteBooking(booking.getBookingId());
        bookingService.assertBookingDeleted(booking.getBookingId());
    }
}
