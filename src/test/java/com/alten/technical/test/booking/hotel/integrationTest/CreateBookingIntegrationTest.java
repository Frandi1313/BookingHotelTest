package com.alten.technical.test.booking.hotel.integrationTest;

import com.alten.technical.test.booking.hotel.integrationTest.utils.IntegrationTestUtils;
import com.alten.technical.test.booking.hotel.response.BookingResponse;
import com.alten.technical.test.booking.hotel.response.CustomerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CreateBookingIntegrationTest {

    @LocalServerPort
    private int port;

    private final IntegrationTestUtils integrationTestUtils = new IntegrationTestUtils();

    private final HttpHeaders headers = new HttpHeaders();

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new JavaTimeModule())
            .build();

    String customerId;

    @Before
    public void setUp() throws Exception {
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> responseEntity = integrationTestUtils.createCustomer(port);

        customerId = mapper.readValue(responseEntity.getBody(), CustomerResponse.class).getId();

    }


    @Test
    public void createBookingTest() throws Exception {

        ResponseEntity<String> response = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                customerId,
                port);

        assertThat(response.getBody()).isNotEmpty();
        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());

        HttpEntity<String> getEntity = new HttpEntity<>(null, headers);

        String bookingId = mapper.readValue(response.getBody(), BookingResponse.class).getId();

        ResponseEntity<String> booking = integrationTestUtils.getBooking(customerId, getEntity, bookingId, port);

        BookingResponse resBooking = mapper.readValue(booking.getBody(), BookingResponse.class);

        assertThat(resBooking).isNotNull();
        assertThat(resBooking.getId()).isEqualTo(bookingId);
        assertThat(resBooking.getInitialDate()).isEqualTo(LocalDate.now().plusDays(1).toString());
        assertThat(resBooking.getFinalDate()).isEqualTo(LocalDate.now().plusDays(2).toString());
        assertThat(resBooking.getCustomerId()).isEqualTo(customerId);

    }


    @Test
    public void createBookingTestMoreThanThreeDays() throws Exception {

        ResponseEntity<String> response = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(13),
                customerId,
                port);

        assertThat(response.getBody()).isNotEmpty();
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());

    }

    @Test
    public void createBookingTestMoreThan30DaysInAdvance() throws Exception {

        ResponseEntity<String> response = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(31),
                LocalDate.now().plusDays(31),
                customerId,
                port);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());

    }

    @Test
    public void createTwoBookingsForTheSameDayTest() throws Exception {

        ResponseEntity<String> response = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(15),
                LocalDate.now().plusDays(15),
                customerId,
                port);

        assertThat(response.getBody()).isNotEmpty();
        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());

        ResponseEntity<String> responseSecond = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(15),
                LocalDate.now().plusDays(15),
                customerId,
                port);

        assertThat(responseSecond.getBody()).isNotEmpty();
        assertThat(HttpStatus.CONFLICT).isEqualTo(responseSecond.getStatusCode());

    }

    @Test
    public void createTwoBookingsInTheSameRangeTest() throws Exception {
        ResponseEntity<String> response = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(22),
                customerId,
                port);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());

        ResponseEntity<String> responseSecond = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(20),
                customerId,
                port);
        assertThat(responseSecond.getBody()).isNotEmpty();
        assertThat(HttpStatus.CONFLICT).isEqualTo(responseSecond.getStatusCode());

        ResponseEntity<String> responseThird = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(21),
                LocalDate.now().plusDays(21),
                customerId,
                port);
        assertThat(responseThird.getBody()).isNotEmpty();
        assertThat(HttpStatus.CONFLICT).isEqualTo(responseThird.getStatusCode());

        ResponseEntity<String> responseFourth = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(22),
                customerId,
                port);
        assertThat(responseFourth.getBody()).isNotEmpty();
        assertThat(HttpStatus.CONFLICT).isEqualTo(responseFourth.getStatusCode());

        ResponseEntity<String> responseFifth = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(22),
                LocalDate.now().plusDays(22),
                customerId,
                port);
        assertThat(responseFifth.getBody()).isNotEmpty();
        assertThat(HttpStatus.CONFLICT).isEqualTo(responseFifth.getStatusCode());

    }

    @Test
    public void createBookingForTodayTest() throws Exception {
        ResponseEntity<String> response = integrationTestUtils.createBooking(
                LocalDate.now(),
                LocalDate.now(),
                customerId,
                port
        );
        assertThat(response.getBody()).isNotEmpty();
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());


    }

}
