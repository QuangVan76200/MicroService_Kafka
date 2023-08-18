package com.service.commonservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDTO {

	private String email;

	private String status;

	private double initialBalance;

	private String fullname;

	private String username;
	
	private String numberphone;
	
	private String password;

	private String role;

}
