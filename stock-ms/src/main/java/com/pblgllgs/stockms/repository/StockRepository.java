package com.pblgllgs.stockms.repository;

import com.pblgllgs.stockms.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<WareHouse, Long> {

    Iterable<WareHouse> findByItem(String item);
}
