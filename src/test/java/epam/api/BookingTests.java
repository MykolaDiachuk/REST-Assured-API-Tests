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

    @Test(description = "Verifies that the booking data is created correctly.")
    public void verifyBookingCreatedCorrectlyTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();

        BookingResponseDTO createdBooking = bookingService.createBooking(bookingDTO);

        Assertions.assertThat(bookingDTO)
                .describedAs("Verify booking data created correctly")
                .isEqualTo(createdBooking.getBooking());
    }

    @Test(description = "Verifies that the booking data is retrieved correctly.")
    public void verifyBookingRetrievedCorrectlyTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();

        BookingResponseDTO expected = bookingService.createBooking(bookingDTO);

        BookingDTO actual = bookingService.getBookingById(expected.getBookingId()).as(BookingDTO.class);

        Assertions.assertThat(actual)
                .describedAs("Verify booking data got correctly")
                .isEqualTo(expected.getBooking());
    }

    @Test(description = "Verifies that the booking is successfully deleted and no longer exists in the system.")
    public void verifyBookingDeletedSuccessfullyTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();

        BookingResponseDTO booking = bookingService.createBooking(bookingDTO);

        Response deleteResponse = bookingService.deleteBooking(booking.getBookingId());

        Assertions.assertThat(deleteResponse.statusCode())
                .describedAs("Verify booking deleted successfully")
                .isEqualTo(201);

        Response existenceCheck = bookingService.getBookingById(booking.getBookingId());

        Assertions.assertThat(existenceCheck.statusCode())
                .describedAs("Verify booking does not exist anymore")
                .isEqualTo(404);
    }

    @Test(description = "Verifies that partial updates (PATCH) to the booking are applied correctly.")
    public void verifyPartialBookingUpdateTest() {
        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();
        BookingResponseDTO createdBooking = bookingService.createBooking(bookingDTO);

        BookingDTO partialBookingDTO = new BookingDTO();

        partialBookingDTO.setFirstname("UpdatedFirstName");
        partialBookingDTO.setLastname("UpdatedLastName");

        Response patchResponse = bookingService.updateBookingWithPatch(createdBooking.getBookingId(), partialBookingDTO);
        Assertions.assertThat(patchResponse.statusCode()).isEqualTo(200);

        BookingDTO updatedBooking = patchResponse.as(BookingDTO.class);

        Assertions.assertThat(updatedBooking.getFirstname()).isNotEqualTo(createdBooking.getBooking().getFirstname());
        Assertions.assertThat(updatedBooking.getLastname()).isNotEqualTo(createdBooking.getBooking().getLastname());
    }

    @Test(description = "Verifies that full updates (PUT) to the booking are applied correctly.")
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

        BookingDTO updatedBooking = putResponse.as(BookingDTO.class);

        Assertions.assertThat(updatedBooking).isEqualTo(fullBookingDTO);
    }
}
