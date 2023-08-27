package com.service.acountservice.accountservice.service;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.acountservice.accountservice.dao.IAccountDao;
import com.service.acountservice.accountservice.data.Account;
import com.service.acountservice.accountservice.model.AccountDTO;
import com.service.acountservice.accountservice.utils.PBKDF2Encoder;
import com.service.commonservice.common.CommonException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountService {

	private IAccountDao accountDao;

	private PasswordEncoder passwordEncoder;;

	public AccountService(IAccountDao accountDao, PasswordEncoder passwordEncoder) {
		this.accountDao = accountDao;
		this.passwordEncoder = passwordEncoder;
	}

	public Mono<Boolean> checkDuplicate(String email) {

		return accountDao.findByEmail(email).flatMap(profile -> Mono.just(true)).switchIfEmpty(Mono.just(false));

	}

	public Mono<Boolean> checkDuplicateUserName(String username) {

		return accountDao.findByUsername(username).flatMap(profile -> Mono.just(true)).switchIfEmpty(Mono.just(false));

	}

	public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
		log.info("Print to Test save in Database");
		return Mono.just(accountDTO).map(AccountDTO::dtoToEnity).flatMap(account -> {
			return checkDuplicateUserName(account.getUsername()).flatMap(isUserName -> {
				if (Boolean.TRUE.equals(isUserName)) {
					throw new CommonException("PD02 Error", "Username already use", HttpStatus.INTERNAL_SERVER_ERROR);
				}
				String encoder = passwordEncoder.encode(account.getPassword());
				account.setPassword(encoder);
				return accountDao.save(account);
			});

		}).map(AccountDTO::entityToDTO).doOnError(throwable -> log.error(throwable.getMessage()));
	}

//	public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
//		log.info("Print to Test save in Database");
//		return Mono.just(accountDTO).map(AccountDTO::dtoToEnity).flatMap(account -> accountDao.save(account))
//				.map(AccountDTO::entityToDTO).doOnError(throwable -> log.error(throwable.getMessage()));
//	}

	public Mono<AccountDTO> getUser(Long userId) {
		return accountDao.findById(userId).map(AccountDTO::entityToDTO)
				.switchIfEmpty(Mono.error(new CommonException("A01", "Account Not Found", HttpStatus.NOT_FOUND)));
	}

	public Mono<AccountDTO> checkBalance(Long id) {
		return findById(id);
	}

	public Mono<AccountDTO> findById(Long id) {
		return accountDao.findById(id).map(AccountDTO::entityToDTO)
				.switchIfEmpty(Mono.error(new CommonException("A01", "Account not found", HttpStatus.NOT_FOUND)));
	}

	public Mono<Boolean> bookAmount(double amount, Long accountId) {
    	return getUser(accountId)
    			.map(AccountDTO::dtoToEnity)
    			.flatMap(accountDTO -> {
    				if(accountDTO.getBalance() < amount + accountDTO.getReserved()) {
    					throw new CommonException("A02", "Balance is not enoungh", HttpStatus.BAD_REQUEST);
    				}
    				accountDTO.setReserved(accountDTO.getReserved()+ amount);
    				return accountDao.findById(accountId).flatMap(existingAccount -> {
    					existingAccount.setReserved(accountDTO.getReserved());
    					return accountDao.save(existingAccount);
    				});
    				
    			})
    			.flatMap(account -> 
    				Mono.just(true)
    			);
    }
}
