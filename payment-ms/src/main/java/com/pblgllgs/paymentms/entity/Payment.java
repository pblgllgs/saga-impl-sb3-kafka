package com.pblgllgs.paymentms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue
    private Long id;
    private String mode;
    @Column(name = "order_id")
    private Long orderId;
    private double amount;
    private String status;

}
