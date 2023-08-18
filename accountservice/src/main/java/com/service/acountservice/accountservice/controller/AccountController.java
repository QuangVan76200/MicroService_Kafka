package com.service.acountservice.accountservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.acountservice.accountservice.dao.IAccountDao;
import com.service.acountservice.accountservice.model.AccountDTO;
import com.service.acountservice.accountservice.model.AuthRequest;
import com.service.acountservice.accountservice.model.AuthResultDto;
import com.service.acountservice.accountservice.service.AccountService;
//import com.service.acountservice.accountservice.security.JWTUtil;
import com.service.acountservice.accountservice.service.SecurityService;
import com.service.acountservice.accountservice.utils.PBKDF2Encoder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/auth/account")
public class AccountController {

//	private JWTUtil jwtUtil;
	private IAccountDao accountDao;
	private PBKDF2Encoder passwordEncoder;
	private AccountService accountService;

	private final SecurityService securityService;

	public AccountController(IAccountDao accountDao, PBKDF2Encoder passwordEncoder, SecurityService securityService,
			AccountService accountService) {
//		this.jwtUtil = jwtUtil;
		this.accountDao = accountDao;
		this.passwordEncoder = passwordEncoder;
		this.securityService = securityService;
		this.accountService = accountService;
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<Object>> login(@RequestBody AuthRequest dto) {
		return securityService.authenticate(dto.getUsername(), dto.getPassword())
				.flatMap(tokenInfo -> Mono.just(ResponseEntity
						.ok(AuthResultDto.builder().userId(tokenInfo.getUserId()).token(tokenInfo.getToken())
								.issuedAt(tokenInfo.getIssuedAt()).expiresAt(tokenInfo.getExpiresAt()).build())));
	}

	@GetMapping(value = "/checkBalance/{id}")
	public Mono<AccountDTO> checkBalance(@PathVariable Long id) {
		log.info("Test call api from Payemnt service");
		return accountService.checkBalance(id);
	}

}
