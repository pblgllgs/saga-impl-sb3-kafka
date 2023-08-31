package com.pblgllgs.paymentms.repository;

import com.pblgllgs.paymentms.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Iterable<Payment> findByOrderId(long orderId);
}
