package com.service.acountservice.accountservice.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

	private Long id;
	private String email;
	private String username;
	@JsonIgnore
	private String password;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dateopened;
	private String currency;
	private double balance;
	private double reserved;
	private boolean enabled;
	private String role;

	public static AccountDTO entityToDTO(Account account) {

		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(account.getId());
		accountDTO.setEmail(account.getEmail());
		accountDTO.setUsername(account.getUsername());
		accountDTO.setPassword(account.getPassword());
		accountDTO.setDateopened(account.getDateopened());
		accountDTO.setCurrency(account.getCurrency());
		accountDTO.setReserved(account.getReserved());
		accountDTO.setBalance(account.getBalance());
		accountDTO.setEnabled(account.isEnabled());
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
		account.setEnabled(accountDTO.isEnabled());
		account.setRole(accountDTO.getRole());

		return account;
	}
}
