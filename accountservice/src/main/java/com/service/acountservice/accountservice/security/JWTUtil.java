package com.service.acountservice.accountservice.security;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.service.acountservice.accountservice.data.Account;
import com.service.commonservice.model.ProfileDTO;
import com.service.commonservice.utils.Constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${springbootwebfluxjjwt.jjwt.secret}")
	private String secret;

	@Value("${springbootwebfluxjjwt.jjwt.expiration}")
	private String expirationTime;

//	private Key key;


	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(Account user) {
		List<String> authorities = Collections.singletonList(user.getRole());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(Constant.AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS256, Constant.SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constant.ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .compact();
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
}
