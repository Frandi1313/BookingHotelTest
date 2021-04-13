package com.alten.technical.test.booking.hotel.service;

import com.alten.technical.test.booking.hotel.request.CustomerCreateRequest;
import com.alten.technical.test.booking.hotel.response.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest createRequest);

    CustomerResponse findCustomerById(String id);

}
