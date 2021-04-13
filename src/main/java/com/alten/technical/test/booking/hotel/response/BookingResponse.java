package com.alten.technical.test.booking.hotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    String id;

    String initialDate;

    String finalDate;

    String customerId;

}
