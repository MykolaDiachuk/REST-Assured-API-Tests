package epam.api;

import epam.api.entities.BookingDTO;
import epam.api.entities.BookingResponseDTO;
import epam.api.services.BookingService;
import epam.api.utils.generator.BookingDTOGenerator;
import epam.api.utils.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();

        BookingResponseDTO expected = bookingService.createBooking(bookingDTO);

        BookingResponseDTO actual = bookingService.convertToBookingResponseDTO(
                bookingService.getBookingById(expected.getBookingId()),expected.getBookingId());

        Assertions.assertThat(actual).isEqualTo(expected)
                .describedAs("Verify booking data created correctly");
    }

    @Test()
    public void verifyBookingRetrievedCorrectlyTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();

        BookingResponseDTO expected = bookingService.createBooking(bookingDTO);

        BookingResponseDTO actual = bookingService.convertToBookingResponseDTO(
                bookingService.getBookingById(expected.getBookingId()),expected.getBookingId());

        Assertions.assertThat(actual.getBooking()).isEqualTo(expected.getBooking())
                .describedAs("Verify booking data got correctly");
    }

    @Test()
    public void verifyBookingDeletedSuccessfullyTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();

        BookingResponseDTO booking = bookingService.createBooking(bookingDTO);

        Response deleteResponse = bookingService.deleteBooking(booking.getBookingId());

        Assertions.assertThat(deleteResponse.statusCode()).isEqualTo(201)
                .describedAs("Verify booking deleted successfully");

        Response existenceCheck = bookingService.getBookingById(booking.getBookingId());

        Assertions.assertThat(existenceCheck.statusCode()).isEqualTo(404)
                .describedAs("Verify booking does not exist anymore");
    }

    @Test
    public void verifyPartialBookingUpdateTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();
        BookingResponseDTO createdBooking = bookingService.createBooking(bookingDTO);

        BookingDTO partialBookingDTO = new BookingDTO(
                "UpdatedFirstName",
                "UpdatedLastName",
                createdBooking.getBooking().getTotalPrice(),
                createdBooking.getBooking().isDepositPaid(),
                createdBooking.getBooking().getBookingDates(),
                createdBooking.getBooking().getAdditionalNeeds()
        );

        Response patchResponse = bookingService.updateBookingWithPatch(createdBooking.getBookingId(), partialBookingDTO);
        Assertions.assertThat(patchResponse.statusCode()).isEqualTo(200);

        BookingDTO updatedBooking = bookingService.getBookingById(
                createdBooking.getBookingId()).as(BookingDTO.class);

        Assertions.assertThat(updatedBooking.getFirstname()).isNotEqualTo(createdBooking.getBooking().getFirstname());
        Assertions.assertThat(updatedBooking.getLastname()).isNotEqualTo(createdBooking.getBooking().getLastname());
    }

    @Test
    public void verifyFullBookingUpdateTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();
        BookingResponseDTO createdBooking = bookingService.createBooking(bookingDTO);

        BookingDTO fullBookingDTO = new BookingDTO(
                "UpdatedFullFirstName",
                "UpdatedFullLastName",
                999,
                true,
                new BookingDTO.BookingDates("2025-06-01", "2025-06-10"),
                "New additional needs"
        );

        Response putResponse = bookingService.updateBookingWithPut(createdBooking.getBookingId(), fullBookingDTO);
        Assertions.assertThat(putResponse.statusCode()).isEqualTo(200);

        BookingDTO updatedBooking = bookingService.getBookingById(
                createdBooking.getBookingId()).as(BookingDTO.class);

        Assertions.assertThat(updatedBooking.getFirstname()).isEqualTo("UpdatedFullFirstName");
        Assertions.assertThat(updatedBooking.getLastname()).isEqualTo("UpdatedFullLastName");
        Assertions.assertThat(updatedBooking.getTotalPrice()).isEqualTo(999);
        Assertions.assertThat(updatedBooking.isDepositPaid()).isEqualTo(true);
        Assertions.assertThat(updatedBooking.getBookingDates().getCheckin()).isEqualTo("2025-06-01");
        Assertions.assertThat(updatedBooking.getBookingDates().getCheckout()).isEqualTo("2025-06-10");
        Assertions.assertThat(updatedBooking.getAdditionalNeeds()).isEqualTo("New additional needs");
    }
}
