package com.pblgllgs.deliveryms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pblgllgs.deliveryms.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @KafkaListener(topics = "new-stock", groupId = "stock-group")
    public void deliverOrder(String event) throws JsonProcessingException {
        deliveryService.deliverOrder(event);
    }
}