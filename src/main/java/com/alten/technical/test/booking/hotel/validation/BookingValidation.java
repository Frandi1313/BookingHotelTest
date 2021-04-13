package com.alten.technical.test.booking.hotel.validation;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingValidation {


    public static void validateCreateRequest(LocalDate initialDate, LocalDate finalDate) {

        LocalDate now = LocalDate.now();

        Validate.isTrue(now.isBefore(initialDate), "The initial date must not be before tomorrow");

        Validate.isTrue(DAYS.between(initialDate, finalDate) < 4, "You can not book more than 3 days");

        Validate.isTrue(DAYS.between(now, finalDate) < 31, "You can not book more than 30 days in advance");

    }


}
