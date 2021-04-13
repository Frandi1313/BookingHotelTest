package com.alten.technical.test.booking.hotel.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utilities {


    public static LocalDate parsingLocalDates(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDate parsingBasicLocalDates(String date) {
        return LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
