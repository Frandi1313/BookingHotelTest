package com.alten.technical.test.booking.hotel.service;

import com.alten.technical.test.booking.hotel.domain.Customer;
import com.alten.technical.test.booking.hotel.repository.CustomerRepository;
import com.alten.technical.test.booking.hotel.request.CustomerCreateRequest;
import com.alten.technical.test.booking.hotel.response.CustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest createRequest) {

        Customer customer = customerRepository.save(new Customer(createRequest.getCustomerName(), createRequest.getCustomerCif()));

        return new CustomerResponse(String.valueOf(customer.getId()), customer.getCustomerCif(), customer.getCustomerName());
    }

    @Override
    public CustomerResponse findCustomerById(String id) {

        Customer customer = customerRepository.findById(Long.parseLong(id)).orElseThrow(EntityNotFoundException::new);

        return new CustomerResponse(String.valueOf(customer.getId()), customer.getCustomerCif(), customer.getCustomerName());

    }
}
