package com.service.profileservice.utils;

import java.util.HashMap;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;

public class ServiceUtils {

	private ServiceUtils() {

	}

	public static Mono<ResponseEntity<String>> getResponse(String message, HttpStatus httpStatus) {
		return Mono.just(new ResponseEntity<String>(message, httpStatus));
	}

//	public static ResponseEntity<Object> getResponseData(String message, HttpStatus httpStatus,
//			Flux<?> listAllProfileDTO) {
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", message);
//
//		response.put("data", listAllProfileDTO.collectList().block());
//
//		return ResponseEntity.status(httpStatus).body(response);
//	}

	public static <T> Mono<Object> getResponseData(String message, HttpStatus httpStatus, Publisher<T> data) {
        return Mono.from(data)
                .map(list -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", message);
                    response.put("data", list);
                    return response;
                })
                .map(response -> new ResponseEntity<>(response, httpStatus));
    }

}
