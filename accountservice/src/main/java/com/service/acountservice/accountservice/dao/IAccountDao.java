package com.service.acountservice.accountservice.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.service.acountservice.accountservice.data.Account;

public interface IAccountDao extends ReactiveCrudRepository<Account, String> {
	

}
