package com.service.acountservice.accountservice.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
}

