package com.service.acountservice.accountservice.service;

import org.springframework.stereotype.Service;

import com.service.acountservice.accountservice.dao.IAccountDao;
import com.service.acountservice.accountservice.model.AccountDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountService {

	private IAccountDao accountDao;

	public AccountService(IAccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
		log.info("Print to Test save in Database");
		return Mono.just(accountDTO).map(AccountDTO::dtoToEnity).flatMap(account -> accountDao.save(account))
				.map(AccountDTO::entityToDTO).doOnError(throwable -> log.error(throwable.getMessage()));
	}
}
