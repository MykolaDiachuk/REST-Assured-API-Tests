package epam.api.tests.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    @JsonProperty("bookingid")
    private int bookingId;
    @JsonProperty("booking")
    private BookingDTO booking;


}