package com.service.profileservice.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Profile {

	@Id
	private long id;
	private String email;
	private String fullname;
	private String numberphone;
	private String status;
	private String role;

}
