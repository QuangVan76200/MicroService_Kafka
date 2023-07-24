package com.service.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDTO {

	private long id;
	private String email;
	private String status;
	private double initialBalance;
	private String name;
	private String role;

}
