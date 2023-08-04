package com.service.acountservice.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.service.acountservice.accountservice.security.AuthenticationManager;
import com.service.acountservice.accountservice.security.SecurityContextRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class WebSecurityConfig {

//	@Bean
//	public PasswordEncoder encoder() {
//		 return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}

	private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;

	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
//		return http.authorizeExchange().pathMatchers("/api/auth/profiles/sign-up").permitAll() // Cho phép tất cả yêu cầu tới
																						// "/api/auth/sign-up" mà không
																						// cần CSRF token
//				.anyExchange().authenticated() // Yêu cầu xác thực (login) cho các yêu cầu khác
//				.and().httpBasic().disable() // Vô hiệu hóa HTTP Basic authentication
//				.formLogin().disable() // Vô hiệu hóa Form-based authentication
//				.csrf().disable() // Vô hiệu hóa CSRF
//				.build();
		
		  return http
	                .exceptionHandling()
	                .authenticationEntryPoint((swe, e) -> 
	                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
	                ).accessDeniedHandler((swe, e) -> 
	                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
	                ).and()
	                .csrf().disable()
	                .formLogin().disable()
	                .httpBasic().disable()
	                .authenticationManager(authenticationManager)
	                .securityContextRepository(securityContextRepository)
	                .authorizeExchange()
	                .pathMatchers(HttpMethod.OPTIONS).permitAll()
	                .pathMatchers("/api/auth/profiles/sign-up", "/api/auth/account/login").permitAll()
	                .anyExchange().authenticated()
	                .and().build();
	}
}

