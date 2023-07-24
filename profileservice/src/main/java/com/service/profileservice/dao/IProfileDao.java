package com.service.profileservice.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.service.profileservice.data.Profile;

import reactor.core.publisher.Mono;

public interface IProfileDao extends ReactiveCrudRepository<Profile, Long> {

	public Mono<Profile> findByEmail(String email);

}
