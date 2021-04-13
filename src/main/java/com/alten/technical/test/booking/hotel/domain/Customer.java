package com.alten.technical.test.booking.hotel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    public String customerName;

    public String customerCif;

    public Customer(String customerName, String customerCif) {
        this.customerCif = customerCif;
        this.customerName = customerName;
    }
}
