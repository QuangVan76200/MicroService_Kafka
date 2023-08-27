package com.service.commonservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

	private long id;
	private Long accountId;
	private double amount;
	private String status;
	
}
