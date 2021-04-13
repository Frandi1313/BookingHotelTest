package com.alten.technical.test.booking.hotel.integrationTest.utils;

import com.alten.technical.test.booking.hotel.request.BookingCreateRequest;
import com.alten.technical.test.booking.hotel.request.BookingUpdateRequest;
import com.alten.technical.test.booking.hotel.request.CustomerCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;


public class IntegrationTestUtils {

    private static final String BOOKING_API = "/booking";
    private static final String CUSTOMER_API = "/customer";
    private static final String AVAILABILITY_API = "/availability";
    private static final String CHECK_API = "/check";


    private final TestRestTemplate restTemplate;

    private final HttpHeaders headers;

    private final ObjectMapper mapper;

    public IntegrationTestUtils() {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new JavaTimeModule())
                .build();

        restTemplate = new TestRestTemplate();
    }

    public ResponseEntity<String> createCustomer(int port) throws Exception {

        CustomerCreateRequest req = new CustomerCreateRequest(
                "10558-SIN",
                "Francesc Andres Diaz"
        );

        HttpEntity<String> createEntity = new HttpEntity<>(mapper.writeValueAsString(req), headers);

        return restTemplate.exchange(createURLWithPort(CUSTOMER_API, port), HttpMethod.POST, createEntity, String.class);
    }

    public ResponseEntity<String> createBooking(LocalDate initialDate, LocalDate finalDate, String customerId, int port) throws Exception {
        BookingCreateRequest req = new BookingCreateRequest(
                initialDate.toString(),
                finalDate.toString()
        );

        HttpEntity<String> createEntity = new HttpEntity<>(mapper.writeValueAsString(req), headers);

        return restTemplate.exchange(createURLWithPort(CUSTOMER_API + "/" + customerId + BOOKING_API, port), HttpMethod.POST, createEntity, String.class);
    }


    public ResponseEntity<String> getBooking(String customerId, HttpEntity<String> getEntity, String bookingId, int port) {

        return restTemplate.exchange(
                createURLWithPort(CUSTOMER_API + "/" + customerId + BOOKING_API + "/" + bookingId, port),
                HttpMethod.GET,
                getEntity,
                String.class);
    }


    public ResponseEntity<String> updateBooking(String bookingId, LocalDate initialDate, LocalDate finalDate, String customerId, int port) throws Exception {
        BookingUpdateRequest req = new BookingUpdateRequest(
                initialDate.toString(),
                finalDate.toString()
        );

        HttpEntity<String> createEntity = new HttpEntity<>(mapper.writeValueAsString(req), headers);

        return restTemplate.exchange(createURLWithPort(CUSTOMER_API + "/" + customerId + BOOKING_API + "/" + bookingId, port), HttpMethod.PUT, createEntity, String.class);
    }


    public ResponseEntity<String> deleteBooking(String bookingId, String customerId, int port) {
        HttpEntity<String> createEntity = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                createURLWithPort(CUSTOMER_API + "/" + customerId + BOOKING_API + "/" + bookingId, port),
                HttpMethod.DELETE, createEntity, String.class);
    }

    public ResponseEntity<String> checkAvailability(String initialDate, String finalDAte, int port) {

        HttpEntity<String> createEntity = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                createURLWithPort(AVAILABILITY_API + CHECK_API + "/" + initialDate + "/" + finalDAte, port)
                , HttpMethod.GET, createEntity, String.class);
    }


    private String createURLWithPort(String uri, int port) {
        return "http://localhost:" + port + uri;
    }
}