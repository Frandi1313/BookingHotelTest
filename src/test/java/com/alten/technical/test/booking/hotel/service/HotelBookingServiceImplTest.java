package com.alten.technical.test.booking.hotel.service;

import com.alten.technical.test.booking.hotel.domain.Booking;
import com.alten.technical.test.booking.hotel.domain.Customer;
import com.alten.technical.test.booking.hotel.repository.BookingRepository;
import com.alten.technical.test.booking.hotel.repository.CustomerRepository;
import com.alten.technical.test.booking.hotel.request.BookingCreateRequest;
import com.alten.technical.test.booking.hotel.request.BookingUpdateRequest;
import com.alten.technical.test.booking.hotel.response.BookingResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class HotelBookingServiceImplTest {

    @InjectMocks
    private HotelBookingServiceImpl hotelBookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    PodamFactory podamFactory = new PodamFactoryImpl();

    Booking bookingSaved;
    Customer customer = podamFactory.manufacturePojo(Customer.class);


    @Before
    public void setUp() {
        createBookingDomain();

        Mockito.when(bookingRepository.save(any())).thenReturn(bookingSaved);
        Mockito.when(bookingRepository.updateBooking(anyLong(), anyLong(), any(), any())).thenReturn(1);
        Mockito.when(bookingRepository.deleteBookingByIdAndCustomerId(anyLong(), anyLong())).thenReturn(1);

        Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
    }


    @Test
    public void createReservationOkTest() {


        Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        BookingResponse bookingResponse = hotelBookingService.createReservation(customer.getId(), createBookingCreateRequest());

        assertNotNull(bookingResponse);
        assertEquals(LocalDate.of(2021, 4, 19).toString(), bookingResponse.getInitialDate());
        assertEquals(LocalDate.of(2021, 4, 21).toString(), bookingResponse.getFinalDate());
        assertEquals(bookingSaved.getCustomer().getId(), Long.parseLong(bookingResponse.getCustomerId()));
    }

    @Test
    public void modifyReservation() {

        hotelBookingService.modifyReservation(100L, 2L, createBookingUpdateRequest());

        Mockito.verify(bookingRepository, Mockito.times(1)).updateBooking(anyLong(), anyLong(), any(), any());

    }

    @Test(expected = IllegalArgumentException.class)
    public void modifyReservationError() {

        Mockito.when(bookingRepository.updateBooking(anyLong(), anyLong(), any(), any())).thenReturn(0);

        hotelBookingService.modifyReservation(10L, 2L, createBookingUpdateRequest());

    }

    @Test
    public void cancelReservation() {

        hotelBookingService.cancelReservation(102L, 2L);

        Mockito.verify(bookingRepository, Mockito.times(1)).deleteBookingByIdAndCustomerId(anyLong(), anyLong());

    }


    @Test(expected = EntityNotFoundException.class)
    public void cancelReservationEntityNotFoundException() {

        Mockito.when(bookingRepository.deleteBookingByIdAndCustomerId(anyLong(), anyLong())).thenReturn(0);

        hotelBookingService.cancelReservation(102L, 2L);

    }


    @Test
    public void isHotelAvailableTestEmptyList() {
        Mockito.when(bookingRepository.findAllBetween(any(), any())).thenReturn(new ArrayList<>());
        assertTrue(hotelBookingService.isHotelAvailable(LocalDate.of(2021, 4, 19), LocalDate.of(2021, 4, 21)));

    }

    @Test
    public void isHotelAvailableTest() {
        Booking booking = podamFactory.manufacturePojo(Booking.class);
        booking.setInitialDate(LocalDate.of(2021, 4, 18));
        booking.setFinalDate(LocalDate.of(2021, 4, 18));

        Mockito.when(bookingRepository.findAllBetween(any(), any())).thenReturn(Collections.singletonList(booking));

        assertTrue(hotelBookingService.isHotelAvailable(LocalDate.of(2021, 4, 19), LocalDate.of(2021, 4, 21)));

    }


    @Test
    public void isHotelUnAvailableTestRange() {

        Booking booking = podamFactory.manufacturePojo(Booking.class);
        booking.setInitialDate(LocalDate.of(2021, 4, 20));
        booking.setFinalDate(LocalDate.of(2021, 4, 20));
        Mockito.when(bookingRepository.findAllBetween(any(), any())).thenReturn(Collections.singletonList(booking));

        assertFalse(hotelBookingService.isHotelAvailable(LocalDate.of(2021, 4, 19), LocalDate.of(2021, 4, 21)));

    }

    @Test
    public void isHotelUnAvailableTestOtherRange() {

        Booking booking = podamFactory.manufacturePojo(Booking.class);
        booking.setInitialDate(LocalDate.of(2021, 4, 18));
        booking.setFinalDate(LocalDate.of(2021, 4, 20));
        Mockito.when(bookingRepository.findAllBetween(any(), any())).thenReturn(Collections.singletonList(booking));
        assertFalse(hotelBookingService.isHotelAvailable(LocalDate.of(2021, 4, 18), LocalDate.of(2021, 4, 18)));

    }

    @Test
    public void isHotelUnAvailableTestDate() {

        Booking booking = podamFactory.manufacturePojo(Booking.class);
        booking.setInitialDate(LocalDate.of(2021, 4, 20));
        booking.setFinalDate(LocalDate.of(2021, 4, 20));


        Mockito.when(bookingRepository.findAllBetween(any(), any())).thenReturn(Collections.singletonList(booking));

        assertFalse(hotelBookingService.isHotelAvailable(LocalDate.of(2021, 4, 20), LocalDate.of(2021, 4, 20)));

    }


    private void createBookingDomain() {
        bookingSaved = podamFactory.manufacturePojo(Booking.class);
        bookingSaved.setInitialDate(LocalDate.of(2021, 4, 19));
        bookingSaved.setFinalDate(LocalDate.of(2021, 4, 21));
        bookingSaved.setCustomer(podamFactory.manufacturePojo(Customer.class));
    }

    private BookingCreateRequest createBookingCreateRequest() {
        return new BookingCreateRequest(
                LocalDate.of(2021, 4, 19).toString(),
                LocalDate.of(2021, 4, 21).toString());
    }

    private BookingUpdateRequest createBookingUpdateRequest() {
        return new BookingUpdateRequest(
                LocalDate.of(2021, 4, 19).toString(),
                LocalDate.of(2021, 4, 21).toString());
    }
}