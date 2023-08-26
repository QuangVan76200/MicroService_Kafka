package com.service.profileservice.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.service.profileservice.data.Profile;

import reactor.core.publisher.Mono;

public interface IProfileDao extends ReactiveCrudRepository<Profile, Long> {

	public Mono<Profile> findByEmail(String email);
	
	public Mono<Profile> findByNumberphone(String numberPhone);
	
	@Query(value = "delete FROM profile WHERE email = :email")
	public Mono<Void> deleteByEmail(String email);

}
