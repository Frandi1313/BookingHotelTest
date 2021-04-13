package com.alten.technical.test.booking.hotel.integrationTest;

import com.alten.technical.test.booking.hotel.integrationTest.utils.IntegrationTestUtils;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CheckAvailabilityIntegrationTest {

    @LocalServerPort
    private int port;

    private final IntegrationTestUtils integrationTestUtils = new IntegrationTestUtils();

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new JavaTimeModule())
            .build();

    String customerId;

    @Before
    public void setUp() throws Exception {

        ResponseEntity<String> responseEntity = integrationTestUtils.createCustomer(port);
        customerId = mapper.readValue(responseEntity.getBody(), CustomerResponse.class).getId();
    }

    @Test
    public void checkAvailabilityTest() {

        ResponseEntity<String> responseAvailability = integrationTestUtils.checkAvailability(
                LocalDate.now().plusDays(10).format(DateTimeFormatter.BASIC_ISO_DATE),
                LocalDate.now().plusDays(12).format(DateTimeFormatter.BASIC_ISO_DATE),
                port
        );

        assertThat(HttpStatus.OK).isEqualTo(responseAvailability.getStatusCode());
    }

    @Test
    public void checkAvailabilityTestBooked() throws Exception {
        ResponseEntity<String> response = integrationTestUtils.createBooking(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                customerId,
                port);

        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
        assertThat(response.getBody()).isNotEmpty();

        ResponseEntity<String> responseAvailability = integrationTestUtils.checkAvailability(
                LocalDate.now().plusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE),
                LocalDate.now().plusDays(2).format(DateTimeFormatter.BASIC_ISO_DATE),
                port
        );


        assertThat(HttpStatus.NOT_FOUND).isEqualTo(responseAvailability.getStatusCode());

    }


}
