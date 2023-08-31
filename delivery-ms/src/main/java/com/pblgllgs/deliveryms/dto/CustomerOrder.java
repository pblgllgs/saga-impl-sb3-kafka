package com.pblgllgs.deliveryms.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerOrder {

    private String item;

    private int quantity;

    private double amount;

    private String paymentMethod;

    private Long orderId;

    private String address;

}