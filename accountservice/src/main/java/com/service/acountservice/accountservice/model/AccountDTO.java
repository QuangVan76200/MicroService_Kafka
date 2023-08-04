package com.service.acountservice.accountservice.model;

import java.time.LocalDateTime;

import com.service.acountservice.accountservice.data.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

	private String id;
	private String email;
	private String username;
	private String password;
	private LocalDateTime dateopened;
	private String currency;
	private double balance;
	private double reserved;
	private String role;

	public static AccountDTO entityToDTO(Account account) {

		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail(account.getEmail());
		accountDTO.setUsername(account.getUsername());
		accountDTO.setPassword(account.getPassword());
		accountDTO.setDateopened(account.getDateopened());
		accountDTO.setCurrency(account.getCurrency());
		accountDTO.setReserved(account.getReserved());
		accountDTO.setBalance(account.getBalance());
		accountDTO.setRole(account.getRole());

		return accountDTO;

//		return AccountDTO.builder().email(account.getEmail()).username(account.getUsername())
//				.password(account.getPassword()).dateOpened(account.getDateOpened()).currency(account.getCurrency())
//				.balance(account.getBalance()).reserved(account.getReserved()).role(account.getRole()).build();
	}

	public static Account dtoToEnity(AccountDTO accountDTO) {
		Account account = new Account();
		account.setEmail(accountDTO.getEmail());
		account.setUsername(accountDTO.getUsername());
		account.setPassword(accountDTO.getPassword());
		account.setDateopened(accountDTO.getDateopened());
		account.setCurrency(accountDTO.getCurrency());
		account.setReserved(accountDTO.getReserved());
		account.setBalance(accountDTO.getBalance());
		account.setRole(accountDTO.getRole());

		return account;
	}
}
