package com.service.profileservice.service;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.service.commonservice.common.CommonException;
import com.service.commonservice.utils.Constant;
import com.service.profileservice.dao.IProfileDao;
import com.service.profileservice.event.EventProducer;
import com.service.profileservice.model.ProfileDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class ProfileService {
	private IProfileDao iProfileDao;
	private EventProducer eventProducer;
	private Gson gson = new Gson();

	public ProfileService(IProfileDao iProfileDao, EventProducer eventProducer) {
		this.iProfileDao = iProfileDao;
		this.eventProducer = eventProducer;
	}

	public Flux<ProfileDTO> getAllProfile() {

		/*
		 * Flux<ProfileDTO> testData = iProfileDao.findAll().map(profile ->
		 * ProfileDTO.entityToDto(profile));
		 * 
		 * testData.doOnNext(value -> System.out.println("this is value " +
		 * value.toString())).subscribe();
		 */

		return iProfileDao.findAll().map(profile -> ProfileDTO.entityToDto(profile))
				.switchIfEmpty(Mono.error(new Exception("List is empty")));
	}

	public Mono<Boolean> checkDuplicate(String email) {

		return iProfileDao.findByEmail(email).flatMap(profile -> Mono.just(true)).switchIfEmpty(Mono.just(false));

	}

	public Mono<Boolean> checkValidateNumberPhone(String numberPhone) {
		return iProfileDao.findByNumberphone(numberPhone).flatMap(aBoolean -> Mono.just(true))
				.switchIfEmpty(Mono.just(false));
	}

	public Mono<ProfileDTO> newProfile(ProfileDTO profileDTO) {

		return checkDuplicate(profileDTO.getEmail()).flatMap(aBoolean -> {
			if (Boolean.TRUE.equals(aBoolean)) {
				log.info("Loi duoc in");
				throw new CommonException("PD02 Error", "Email already use", HttpStatus.INTERNAL_SERVER_ERROR);

			}
			return checkValidateNumberPhone(profileDTO.getNumberphone()).flatMap(nums -> {
				if (Boolean.TRUE.equals(nums)) {
					throw new CommonException("PD02 Error", "Numberphone already used",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
				log.info("Loi khong duoc in");
				System.out.println("Step 2");
				profileDTO.setStatus(Constant.STATUS_PROFILE_PENDING);
				return createProfile(profileDTO);
			});

		});

	}

	public Mono<ProfileDTO> createProfile(ProfileDTO profileDTO) {
		return Mono.just(profileDTO).map(ProfileDTO::dtoToEntity).flatMap(profile -> iProfileDao.save(profile))
				.map(ProfileDTO::entityToDto).doOnError(throwable -> log.error(throwable.getMessage()))
				.doOnSuccess(dto -> {
					if (Objects.equals(dto.getStatus(), Constant.STATUS_PROFILE_PENDING)) {
						dto.setPassword(profileDTO.getPassword());
						dto.setUsername(profileDTO.getUsername());
						dto.setInitialBalance(profileDTO.getInitialBalance());
						eventProducer.send(Constant.PROFILE_ONBOARDING_TOPIC, gson.toJson(dto)).subscribe();
					}
				});
	}
	
	public Mono<ProfileDTO> updateStatusProfile(ProfileDTO profileDTO) {

		return iProfileDao.findByEmail(profileDTO.getEmail()).flatMap(existProfile -> {
			existProfile.setStatus(profileDTO.getStatus());
			System.out.println("Status "+ profileDTO.getStatus());
			return iProfileDao.save(existProfile).map(ProfileDTO::entityToDto);
		}).doOnError(throwable -> log.error(throwable.getMessage()));
	}

	public Mono<ProfileDTO> updateStatusProfiles(ProfileDTO profileDTO) {

		return checkDuplicate(profileDTO.getEmail()).flatMap(exists -> {
			if (!exists) {
				System.out.println("Updated successfull");
				findByEmail(profileDTO.getEmail()).map(ProfileDTO::dtoToEntity).flatMap(profile -> {
					profile.setStatus(profileDTO.getStatus());
					return iProfileDao.save(profile);
				}).map(ProfileDTO::entityToDto).doOnError(throwable -> log.error(throwable.getMessage()));
			}
			return Mono.empty();
		});
	}

	public Mono<ProfileDTO> findByEmail(String email) {
		return iProfileDao.findByEmail(email).map(ProfileDTO::entityToDto)
				.switchIfEmpty(Mono.error(new CommonException("PF03", "Profile is Not Found", HttpStatus.NOT_FOUND)));
	}

	public Mono<Void> rollbackProfile(String email ) {
		return iProfileDao.deleteByEmail(email); 

	}

}
