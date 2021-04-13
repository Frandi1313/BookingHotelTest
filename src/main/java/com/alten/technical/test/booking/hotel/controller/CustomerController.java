package com.alten.technical.test.booking.hotel.controller;

import com.alten.technical.test.booking.hotel.request.CustomerCreateRequest;
import com.alten.technical.test.booking.hotel.response.CustomerResponse;
import com.alten.technical.test.booking.hotel.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Validated CustomerCreateRequest customerCreateRequest) {
        return new ResponseEntity<>(
                customerService.createCustomer(customerCreateRequest),
                HttpStatus.CREATED);

    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getBooking(@PathVariable("customerId") final String id) {
        return ok(customerService.findCustomerById(id));
    }

}
