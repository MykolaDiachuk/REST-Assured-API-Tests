package epam.api.tests;

import epam.api.tests.entities.BookingDTO;
import epam.api.tests.entities.BookingResponseDTO;
import epam.api.tests.utils.BookingDTOGenerator;
import epam.api.tests.utils.ConfigReader;
import epam.api.tests.utils.HTTPClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BookingTests {
    private static final Logger logger = LoggerFactory.getLogger(BookingTests.class);
    private static final String BASE_URL = ConfigReader.getProperty("base.url");

    private static final ThreadLocal<Integer> bookingId = new ThreadLocal<>();
    private static final ThreadLocal<Response> response = new ThreadLocal<>();
    private static final ThreadLocal<BookingResponseDTO> expectedBooking = new ThreadLocal<>();


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        logger.info("Base URL set to: {}", BASE_URL);
    }


    @BeforeMethod
    public void beforeMethod() {
        logger.info("Create new Booking");

        BookingDTO bookingDTO = BookingDTOGenerator.generateRandomBookingEntity();

        Response res = createBooking(bookingDTO);
        response.set(res);
        bookingId.set(res.jsonPath().getInt("bookingid"));

        BookingResponseDTO bookingResponseDTO =
                new BookingResponseDTO(bookingId.get(),bookingDTO);
        expectedBooking.set(bookingResponseDTO);
    }

    @Test()
    public void testCreateBooking() {
        logger.info("Test Create Booking");

        BookingResponseDTO expectedResult = expectedBooking.get();
        BookingResponseDTO actualResult = response.get().as(BookingResponseDTO.class);

        actualResult.setBookingId(bookingId.get());

        Assertions.assertThat(actualResult).isEqualTo(expectedResult)
                .describedAs("Verify booking data created correctly");
    }

    @Test()
    public void testGetBooking() {
        logger.info("Test Get Booking");
        logger.info("Get Booking by id: {}", bookingId.get());

        Response response = HTTPClient.GET("/booking/" + bookingId.get(), 200);
        BookingDTO actualResult = response.as(BookingDTO.class);

        Assertions.assertThat(actualResult).isEqualTo(expectedBooking.get().getBooking())
                .describedAs("Verify booking data got correctly");
    }

    @Test()
    public void testDeleteBooking() {
        logger.info("Test Delete Booking");
        logger.info("Delete booking with ID: {}", bookingId.get());

        HTTPClient.DELETE("/booking/" + bookingId.get());

        logger.info("Verify that booking with ID {} no longer exists", bookingId.get());
        HTTPClient.GET("/booking/" + bookingId.get(), 404);

    }

    private Response createBooking(BookingDTO bookingDTO) {
        return HTTPClient.POST("/booking", bookingDTO);
    }


}
