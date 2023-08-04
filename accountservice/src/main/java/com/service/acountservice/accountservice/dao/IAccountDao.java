package com.service.acountservice.accountservice.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.service.acountservice.accountservice.data.Account;

import reactor.core.publisher.Mono;

public interface IAccountDao extends ReactiveCrudRepository<Account, String> {

	public Mono<Account> findByEmail(String email);

	public Mono<Account> findByUsername(String username);

}
