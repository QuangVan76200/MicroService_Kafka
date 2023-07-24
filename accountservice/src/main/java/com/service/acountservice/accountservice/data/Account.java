package com.service.acountservice.accountservice.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
	@Id
	private String id;
	private String email;
	private String currency;
	private double balance;
	private double reserved;
	
    @Version
    private Long version;

}