package com.pblgllgs.paymentms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEvent {
    private CustomerOrder order;
    private String type;
}
