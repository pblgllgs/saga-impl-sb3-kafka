package com.pblgllgs.orderms.repository;

import com.pblgllgs.orderms.dto.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order,Long> {
}
