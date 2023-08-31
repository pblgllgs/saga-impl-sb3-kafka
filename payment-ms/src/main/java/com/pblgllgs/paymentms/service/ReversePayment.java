package com.pblgllgs.paymentms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pblgllgs.paymentms.dto.CustomerOrder;
import com.pblgllgs.paymentms.dto.OrderEvent;
import com.pblgllgs.paymentms.dto.PaymentEvent;
import com.pblgllgs.paymentms.entity.Payment;
import com.pblgllgs.paymentms.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ReversePayment {
    private final Logger logger = Logger.getLogger(ReversePayment.class.getName());
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @KafkaListener(groupId = "payments-group", topics = "reversed-payments")
    public void reversePayment(String event) {
        logger.log(Level.WARNING, MessageFormat.format("Reverse payment event {0}", event));
        try {
            PaymentEvent paymentEvent = new ObjectMapper().readValue(event, PaymentEvent.class);
            CustomerOrder customerOrder = paymentEvent.getOrder();
            Iterable<Payment> payments = paymentRepository.findByOrderId(customerOrder.getOrderId());

            payments.forEach(p -> {
                p.setStatus("FAILED");
                paymentRepository.save(p);
            });

            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrder(paymentEvent.getOrder());
            orderEvent.setType("ORDER_REVERSED");
            kafkaTemplate.send("reversed-orders",orderEvent);
        } catch (Exception e) {
            logger.log(Level.WARNING, MessageFormat.format("Something went wrong went try to reverse payment {0}", e));
        }
    }
}
