package com.pblgllgs.paymentms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pblgllgs.paymentms.dto.CustomerOrder;
import com.pblgllgs.paymentms.dto.OrderEvent;
import com.pblgllgs.paymentms.dto.PaymentEvent;
import com.pblgllgs.paymentms.entity.Payment;
import com.pblgllgs.paymentms.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;
    private final KafkaTemplate<String, OrderEvent> orderKafkaTemplate;
    private final Logger logger = Logger.getLogger(PaymentService.class.getName());

    public void payment(String event) throws JsonProcessingException {
        OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
        CustomerOrder customerOrder = orderEvent.getOrder();
        Payment payment = new Payment();
        payment.setAmount(customerOrder.getAmount());
        payment.setMode(customerOrder.getPaymentMethod());
        payment.setOrderId(customerOrder.getOrderId());
        payment.setStatus("SUCCESS");
        try {
            paymentRepository.save(payment);
            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setOrder(customerOrder);
            paymentEvent.setType("PAYMENT_CREATED");
            paymentKafkaTemplate.send("new-payments", paymentEvent);
            logger.log(Level.INFO, MessageFormat.format("Process payment event: {0}", event));
        } catch (Exception e) {
            payment.setOrderId(customerOrder.getOrderId());
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            OrderEvent orderEventFailed = new OrderEvent();
            orderEventFailed.setOrder(customerOrder);
            orderEventFailed.setType("ORDER_REVERSED");
            orderKafkaTemplate.send("reversed-orders", orderEventFailed);
            logger.log(Level.WARNING, MessageFormat.format("Something went wrong, payment failed: {0}", e));
        }
    }
}
