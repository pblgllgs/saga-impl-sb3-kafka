package com.pblgllgs.paymentms.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentEvent {
    private CustomerOrder order;
    private String type;
}
