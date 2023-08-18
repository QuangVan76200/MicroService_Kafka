package com.service.paymentservice.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.service.paymentservice.data.Payment;

import reactor.core.publisher.Flux;

public interface IPaymentDao extends ReactiveCrudRepository<Payment, Long> {

	@Query("SELECT * FROM payment WHERE account_id = :id")
	Flux<Payment> findByAccountId(Long id);
}
