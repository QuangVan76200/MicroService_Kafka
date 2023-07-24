package com.service.acountservice.accountservice.model;

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
	private String currency;
	private double balance;
	private double reserved;

	public static AccountDTO entityToDTO(Account account) {
		return AccountDTO.builder().email(account.getEmail()).currency(account.getCurrency())
				.balance(account.getBalance()).reserved(account.getReserved()).build();
	}

	public static Account dtoToEnity(AccountDTO accountDTO) {
		Account account = new Account();
		account.setEmail(accountDTO.getEmail());
		account.setCurrency(accountDTO.getCurrency());
		account.setReserved(accountDTO.getReserved());
		account.setBalance(accountDTO.getBalance());

		return account;
	}
}
