package com.alten.technical.test.booking.hotel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Booking")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    public Customer customer;

    private LocalDate initialDate;

    private LocalDate finalDate;

    private boolean isActive;

    public Booking(Customer customer, LocalDate initialDate, LocalDate finalDate, boolean isActive) {
        this.customer = customer;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.isActive = isActive;
    }
}
