package com.pblgllgs.stockms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pblgllgs.stockms.dto.*;
import com.pblgllgs.stockms.entity.WareHouse;
import com.pblgllgs.stockms.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final Logger logger = Logger.getLogger(StockService.class.getName());
    private final KafkaTemplate<String, DeliveryEvent> kafkaTemplate;
    private final KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;

    public void updateStock(String paymentEvent) throws JsonProcessingException {
        DeliveryEvent deliveryEvent = new DeliveryEvent();
        PaymentEvent p = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
        CustomerOrder customerOrder = p.getOrder();

        try {
            Iterable<WareHouse> inventories = stockRepository.findByItem(customerOrder.getItem());
            boolean exists = inventories.iterator().hasNext();

            if (!exists) {
                logger.log(Level.INFO, "Stock not available");
            }

            inventories.forEach(i -> {
                i.setQuantity(i.getQuantity() - customerOrder.getQuantity());
                stockRepository.save(i);
            });

            deliveryEvent.setOrder(customerOrder);
            deliveryEvent.setType("ORDER_REVERSED");
            kafkaTemplate.send("new-stock", deliveryEvent);
        } catch (Exception e) {
            PaymentEvent paymentEventFailed = new PaymentEvent();
            paymentEventFailed.setOrder(customerOrder);
            paymentEventFailed.setType("PAYMENT_REVERSED");
            paymentKafkaTemplate.send("reversed-payments", paymentEventFailed);
            logger.log(Level.WARNING, MessageFormat.format("Something went wrong, payment failed: {0}", e));
        }
    }

    public void addItems(Stock stock) {
        Iterable<com.pblgllgs.stockms.entity.WareHouse> items =  stockRepository.findByItem(stock.getItem());

        if (items.iterator().hasNext()){
            items.forEach( i-> {
                i.setQuantity(stock.getQuantity() + i.getQuantity());
                stockRepository.save(i);
            });
        }else {
            WareHouse i =  new WareHouse();
            i.setItem(stock.getItem());
            i.setQuantity(stock.getQuantity());
            stockRepository.save(i);
        }
    }
}
