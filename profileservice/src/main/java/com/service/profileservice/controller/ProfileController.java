package com.service.profileservice.controller;

import java.io.InputStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.service.commonservice.utils.CommonFunction;
import com.service.commonservice.utils.PageSupport;
import com.service.profileservice.constant.Constant;
import com.service.profileservice.model.ProfileDTO;
import com.service.profileservice.service.ProfileService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.service.commonservice.utils.PageSupport.DEFAULT_PAGE_SIZE;
import static com.service.commonservice.utils.PageSupport.FIRST_PAGE_NUM;;

@Slf4j
@RestController
@RequestMapping("/api/auth/profiles/")
public class ProfileController {

	private ProfileService profileService;

	Gson gson = new Gson();

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@GetMapping("/findall")
	public Mono<PageSupport<ProfileDTO>> getAllProfile( @RequestParam(name = "page", defaultValue = FIRST_PAGE_NUM) int page,
												 @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {

		return profileService.getAllProfile(PageRequest.of(page, size));
		
	}

	@GetMapping("/checkduplicate/{email}")
	public ResponseEntity<Mono<Boolean>> checkDuplicate(@PathVariable String email) {

		Mono<Boolean> checkDuplicateEmail = profileService.checkDuplicate(email);

		return ResponseEntity.ok(checkDuplicateEmail);

	}
	
	@GetMapping("/checkduplicate/numberphone/{numberphone}")
	public ResponseEntity<Mono<Boolean>> checkDuplicateNumberphone(@PathVariable String numberphone) {
		Mono<Boolean> checkDuplicateNumberphone = profileService.checkValidateNumberPhone(numberphone);
		return ResponseEntity.ok(checkDuplicateNumberphone);

	}

	@PostMapping("/sign-up")
	public ResponseEntity<Mono<ProfileDTO>> newProfile(@RequestBody String requestStr) {
		InputStream inputStream = ProfileController.class.getClassLoader()
				.getResourceAsStream(Constant.JSON_REQ_CREATE_PROFILE);
		CommonFunction.jsonValidate(inputStream, requestStr);
		Mono<ProfileDTO> createProfile = profileService.newProfile(gson.fromJson(requestStr, ProfileDTO.class));
		return ResponseEntity.status(HttpStatus.CREATED).body(createProfile);
	}

}
