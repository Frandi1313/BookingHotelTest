package com.alten.technical.test.booking.hotel.service;

import com.alten.technical.test.booking.hotel.request.BookingCreateRequest;
import com.alten.technical.test.booking.hotel.request.BookingUpdateRequest;
import com.alten.technical.test.booking.hotel.response.BookingResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public interface HotelBookingService {

    BookingResponse createReservation(final Long customerId, BookingCreateRequest bookingCreateRequest);

    void modifyReservation(final Long id, final Long customerId, BookingUpdateRequest bookingUpdateRequest);

    void cancelReservation(final Long id, final Long customerId);

    BookingResponse findBookingById(final Long id);

    boolean isHotelAvailable(LocalDate initialDate, LocalDate finalDate);

    boolean isHotelAvailable(LocalDate initialDate, LocalDate finalDate, Optional<Long> optional);
}
