package epam.api.tests.utils;

import com.github.javafaker.Faker;
import epam.api.tests.entities.BookingDTO;


import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;



public class BookingDTOGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static BookingDTO generateRandomBookingEntity() {
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        int totalPrice = random.nextInt(1000) + 100;
        boolean depositPaid = random.nextBoolean();

        Date checkinDate = faker.date().future(30, java.util.concurrent.TimeUnit.DAYS);
        Date checkoutDate = faker.date().future(60, java.util.concurrent.TimeUnit.DAYS);
        String formattedCheckinDate = dateFormat.format(checkinDate);
        String formattedCheckoutDate = dateFormat.format(checkoutDate);

        String additionalNeeds = faker.lorem().sentence();

        BookingDTO.BookingDates bookingDates = new BookingDTO.BookingDates(formattedCheckinDate, formattedCheckoutDate);
        BookingDTO bookingDTO = new BookingDTO(
                firstname,
                lastname,
                totalPrice,
                depositPaid,
                bookingDates,
                additionalNeeds
        );

        return bookingDTO;
    }

}
