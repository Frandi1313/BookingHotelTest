package com.alten.technical.test.booking.hotel.service;

import com.alten.technical.test.booking.hotel.domain.Booking;
import com.alten.technical.test.booking.hotel.domain.Customer;
import com.alten.technical.test.booking.hotel.repository.BookingRepository;
import com.alten.technical.test.booking.hotel.repository.CustomerRepository;
import com.alten.technical.test.booking.hotel.request.BookingCreateRequest;
import com.alten.technical.test.booking.hotel.request.BookingUpdateRequest;
import com.alten.technical.test.booking.hotel.response.BookingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.alten.technical.test.booking.hotel.utils.Utilities.parsingLocalDates;


@Service
public class HotelBookingServiceImpl implements HotelBookingService {

    public static final Boolean IS_ACTIVE = Boolean.TRUE;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public BookingResponse createReservation(Long customerId, BookingCreateRequest bookingCreateRequest) {

        Customer customer = getCustomerId(customerId);

        Booking booking = bookingRepository.save(
                new Booking(customer,
                        parsingLocalDates(bookingCreateRequest.getInitialDate()),
                        parsingLocalDates(bookingCreateRequest.getFinalDate()),
                        IS_ACTIVE

                )
        );

        return new BookingResponse(
                String.valueOf(booking.getId()),
                booking.getInitialDate().toString(),
                booking.getFinalDate().toString(),
                String.valueOf(booking.getCustomer().getId())
        );
    }


    @Override
    public void modifyReservation(final Long id, final Long customerId, BookingUpdateRequest bookingUpdateRequest) {

        getCustomerId(customerId);

        int result = bookingRepository.updateBooking(id,
                customerId,
                parsingLocalDates(bookingUpdateRequest.getInitialDate()),
                parsingLocalDates(bookingUpdateRequest.getFinalDate())
        );

        if (result == 0) {
            throw new IllegalArgumentException("Error updating the booking");
        }

    }

    @Override
    public void cancelReservation(final Long id, final Long customerId) {
        if (bookingRepository.deleteBookingByIdAndCustomerId(id, customerId) != 1) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public BookingResponse findBookingById(Long id) {
        Booking booking = bookingRepository.findByIdAndIsActive(id, IS_ACTIVE).orElseThrow(EntityNotFoundException::new);

        return new BookingResponse(
                String.valueOf(booking.getId()),
                booking.getInitialDate().toString(),
                booking.getFinalDate().toString(),
                String.valueOf(booking.getCustomer().getId())
        );
    }

    @Override
    public boolean isHotelAvailable(LocalDate initialDate, LocalDate finalDate) {
        return isHotelAvailable(initialDate, finalDate, Optional.empty());

    }

    @Override
    public boolean isHotelAvailable(LocalDate initialDate, LocalDate finalDate, Optional<Long> bookingId) {

        List<Booking> bookingList = bookingRepository.findAllBetween(initialDate.minusDays(3), finalDate.plusDays(3));

        return bookingList.stream().filter(b -> !bookingId.isPresent() || b.getId() != bookingId.get())
                .noneMatch(isValidRangePredicate(initialDate, finalDate));

    }

    private Predicate<Booking> isValidRangePredicate(LocalDate initialDate, LocalDate finalDate) {
        return b -> (isAfterOrEqualDate(initialDate, b.getInitialDate()) && isBeforeOrEqualDate(finalDate, b.getFinalDate()))
                || (isAfterOrEqualDate(b.getInitialDate(), initialDate) && isBeforeOrEqualDate(b.getFinalDate(), finalDate));
    }


    private boolean isBeforeOrEqualDate(LocalDate initialDate, LocalDate b) {
        return initialDate.isBefore(b) || initialDate.isEqual(b);
    }

    private boolean isAfterOrEqualDate(LocalDate finalDate, LocalDate b) {
        return finalDate.isAfter(b) || finalDate.isEqual(b);
    }

    private Customer getCustomerId(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(EntityNotFoundException::new);
    }


}
