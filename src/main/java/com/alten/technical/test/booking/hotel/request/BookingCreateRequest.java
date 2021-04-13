package com.alten.technical.test.booking.hotel.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {

    @NotNull
    private String initialDate;

    @NotNull
    private String finalDate;


}
