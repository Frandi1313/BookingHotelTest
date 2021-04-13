package com.alten.technical.test.booking.hotel.repository;

import com.alten.technical.test.booking.hotel.domain.Booking;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public interface BookingRepository extends CrudRepository<Booking, Long> {

    @Override
    Booking save(Booking s);

    @Transactional
    @Modifying
    @Query("update Booking b set b.isActive = false  where b.id = :id AND b.customer.id = :customerId")
    int deleteBookingByIdAndCustomerId(@Param("id") final Long id, @Param("customerId") final long customerId);

    Optional<Booking> findByIdAndIsActive(final long id, final boolean isActive);

    @Modifying
    @Transactional
    @Query("update Booking b set b.initialDate = :initialDate, b.finalDate = :finalDate where b.id = :id AND b.customer.id = :customerId AND b.isActive = true")
    int updateBooking(@Param("id") final long id,
                      @Param("customerId") final long customerId,
                      @Param("initialDate") final LocalDate initialDate,
                      @Param("finalDate") final LocalDate finalDate);

    @Query("select b from Booking b where b.initialDate >= :initialDate AND b.finalDate <= :finalDate AND b.isActive = true")
    List<Booking> findAllBetween(@Param("initialDate") LocalDate initialDate, @Param("finalDate") LocalDate finalDate);


}
