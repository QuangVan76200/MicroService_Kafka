package com.service.acountservice.accountservice.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.service.acountservice.accountservice.data.Account;

import reactor.core.publisher.Mono;

public interface IAccountDao extends ReactiveCrudRepository<Account, Long> {

	public Mono<Account> findByEmail(String email);

	@Query("SELECT * FROM account WHERE username = :username")
	public Mono<Account> findByUsername(String username);

}
