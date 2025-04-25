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
public class BookingDTO {
    private String firstname;
    private String lastname;

    @JsonProperty("totalprice")
    private Integer totalPrice;

    @JsonProperty("depositpaid")
    private Boolean depositPaid;

    @JsonProperty("bookingdates")
    private BookingDates bookingDates;

    @JsonProperty("additionalneeds")
    private String additionalNeeds;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BookingDates {
        private String checkin;
        private String checkout;
    }
}
