package com.alten.technical.test.booking.hotel.controller;

import com.alten.technical.test.booking.hotel.exception.RoomAlreadyBookedException;
import com.alten.technical.test.booking.hotel.request.BookingCreateRequest;
import com.alten.technical.test.booking.hotel.request.BookingUpdateRequest;
import com.alten.technical.test.booking.hotel.response.BookingResponse;
import com.alten.technical.test.booking.hotel.service.HotelBookingService;
import com.alten.technical.test.booking.hotel.validation.BookingValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.alten.technical.test.booking.hotel.utils.Utilities.parsingLocalDates;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("customer/{customerId}/booking")
public class HotelBookingController {

    @Autowired
    private HotelBookingService hotelBookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@PathVariable("customerId") Long customerId,
                                                         @RequestBody @Validated BookingCreateRequest bookingCreateRequest) {

        LocalDate initialDateParsed = parsingLocalDates(bookingCreateRequest.getInitialDate());
        LocalDate finalDateParsed = parsingLocalDates(bookingCreateRequest.getFinalDate());

        BookingValidation.validateCreateRequest(initialDateParsed, finalDateParsed);

        checkHotelAvailability(initialDateParsed, finalDateParsed, null);

        return new ResponseEntity<>(
                hotelBookingService.createReservation(customerId, bookingCreateRequest),
                HttpStatus.CREATED);

    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<?> modifyReservation(@PathVariable("customerId") final Long customerId,
                                               @PathVariable("bookingId") final Long bookingIdentifier,
                                               @Validated @RequestBody BookingUpdateRequest bookingUpdateRequest) {

        checkHotelAvailability(parsingLocalDates(bookingUpdateRequest.getInitialDate()),
                parsingLocalDates(bookingUpdateRequest.getFinalDate()), bookingIdentifier
        );

        hotelBookingService.modifyReservation(bookingIdentifier, customerId, bookingUpdateRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> cancelReservation(@PathVariable("customerId") final Long customerId, @PathVariable("bookingId") final Long id) {
        hotelBookingService.cancelReservation(id, customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable("bookingId") final Long id) {
        return ok(hotelBookingService.findBookingById(id));
    }


    private void checkHotelAvailability(LocalDate initialDate, LocalDate finalDate, Long id) {
        if (!hotelBookingService.isHotelAvailable(initialDate, finalDate, Optional.ofNullable(id))) {
            throw new RoomAlreadyBookedException("The room is already booked");
        }
    }

}
