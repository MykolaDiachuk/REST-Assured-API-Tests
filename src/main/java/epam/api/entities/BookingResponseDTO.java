package epam.api.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponseDTO {
    @JsonProperty("bookingid")
    private Integer bookingId;
    @JsonProperty("booking")
    private BookingDTO booking;
}