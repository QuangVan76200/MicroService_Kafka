package com.service.acountservice.accountservice.security.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.service.acountservice.accountservice.security.support.JwtVerifyHandler;

import reactor.core.publisher.Mono;

public class CurrentUserAuthenticationBearer {
    public static Mono<Authentication> create(JwtVerifyHandler.VerificationResult verificationResult) {
    	var claims = verificationResult.claims;
    	var subject = claims.getSubject();
    	String rolesString = claims.get("role", String.class);

    	List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    	if (rolesString != null && !rolesString.isEmpty()) {
    	    authorities.add(new SimpleGrantedAuthority(rolesString));
    	}

    	var principalId = 0L;
        try {
            principalId = Long.parseLong(subject);
        } catch (NumberFormatException ignore) { }

        if (principalId == 0)
            return Mono.empty(); // invalid value for any of jwt auth parts

        var principal = new UserPrincipal(principalId, claims.getIssuer());

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }

}
