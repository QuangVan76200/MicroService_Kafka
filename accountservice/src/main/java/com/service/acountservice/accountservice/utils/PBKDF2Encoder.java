package com.service.acountservice.accountservice.utils;

import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Primary
@Component
public class PBKDF2Encoder implements PasswordEncoder {

	@Value("${springbootwebfluxjjwt.password.encoder.secret}")
	private String secret;

	@Value("${springbootwebfluxjjwt.password.encoder.iteration}")
	private Integer iteration;

	@Value("${springbootwebfluxjjwt.password.encoder.keylength}")
	private Integer keylength;

	@Override
	public String encode(CharSequence cs) {
		try {
			
			System.out.println("secret "+secret);
			System.out.println("iteration "+iteration);
			System.out.println("keylength "+keylength);
			
			byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
					.generateSecret(
							new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keylength))
					.getEncoded();
			String token = Base64.getEncoder().encodeToString(result);
//			System.out.println(token);
			return Base64.getEncoder().encodeToString(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean matches(CharSequence cs, String encodedPassword) {
		// TODO Auto-generated method stub
		return encode(cs).equals(encodedPassword);
	}

}