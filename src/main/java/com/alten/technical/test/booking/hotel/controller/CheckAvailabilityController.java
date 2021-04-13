package com.alten.technical.test.booking.hotel.controller;

import com.alten.technical.test.booking.hotel.service.HotelBookingService;
import com.alten.technical.test.booking.hotel.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/availability")
public class CheckAvailabilityController {

    @Autowired
    private HotelBookingService hotelBookingService;

    @GetMapping("/check/{initialDate}/{finalDate}")
    public ResponseEntity<String> availabilityReservation(
            @PathVariable("initialDate") String initialDate,
            @PathVariable("finalDate") String finalDate) {

        if (!hotelBookingService.isHotelAvailable(
                Utilities.parsingBasicLocalDates(initialDate),
                Utilities.parsingBasicLocalDates(finalDate))
        ) {
            return new ResponseEntity<>(
                    "The room is NOT available for the dates requested",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                "The room is available for the dates requested",
                HttpStatus.OK);
    }
}
