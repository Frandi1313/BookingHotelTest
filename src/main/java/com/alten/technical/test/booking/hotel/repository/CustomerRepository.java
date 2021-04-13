package com.alten.technical.test.booking.hotel.repository;

import com.alten.technical.test.booking.hotel.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Override
    Customer save(Customer s);

    Optional<Customer> findById(final long id);


}
