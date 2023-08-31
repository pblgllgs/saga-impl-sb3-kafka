package com.pblgllgs.stockms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pblgllgs.stockms.dto.Stock;
import com.pblgllgs.stockms.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @KafkaListener(topics = "new-payments", groupId = "payments-group")
    public void updateStock(String event) throws JsonProcessingException {
        stockService.updateStock(event);
    }

    @PostMapping("/addItems")
    public void addItems(@RequestBody Stock stock){
        stockService.addItems(stock);
    }
}
