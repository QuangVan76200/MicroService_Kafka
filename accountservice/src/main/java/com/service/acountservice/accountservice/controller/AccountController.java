package com.service.acountservice.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.acountservice.accountservice.dao.IAccountDao;
import com.service.acountservice.accountservice.model.AuthRequest;
import com.service.acountservice.accountservice.model.AuthResponse;
import com.service.acountservice.accountservice.security.JWTUtil;
import com.service.acountservice.accountservice.utils.PBKDF2Encoder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/auth/account")
public class AccountController {
	
	private JWTUtil jwtUtil;
	private IAccountDao accountDao;
	private PBKDF2Encoder passwordEncoder;
	
	
	
	public AccountController(JWTUtil jwtUtil, IAccountDao accountDao, PBKDF2Encoder passwordEncoder) {
		this.jwtUtil = jwtUtil;
		this.accountDao = accountDao;
		this.passwordEncoder = passwordEncoder;
	}



	@PostMapping("/login")
	public Mono<ResponseEntity<AuthResponse<String>>> login(@RequestBody AuthRequest request) {
//		accountDao.findByUsername(request.getUsername()).flatMap(u -> {
//			boolean isPasswordValid = passwordEncoder.encode(request.getPassword()).equals(u.getPassword());
//			return Mono.just(u);
//		}).subscribe();

		return accountDao.findByUsername(request.getUsername()).map(u -> {
			String reqPass = passwordEncoder.encode(request.getPassword());
			int index  = reqPass.indexOf("/");
			String result = index != -1 ? reqPass.substring(0, index) : reqPass;
			if (result.equals(u.getPassword())) {
				log.info("password is correct, return token");
				String token = jwtUtil.generateToken(u);
				AuthResponse<String> response = new AuthResponse<>("Login successful", token);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				log.info("password is Incorrect, return Fail");
				AuthResponse<String> response = new AuthResponse<>("", "Invalid credentials");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}

		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new AuthResponse<>("", "User Not Found, Please Register before login")));
	}


}
