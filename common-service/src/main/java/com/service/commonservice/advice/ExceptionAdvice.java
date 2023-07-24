package com.service.commonservice.advice;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.service.commonservice.common.CommonException;
import com.service.commonservice.common.ErrorMessage;
import com.service.commonservice.common.ValidateException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j

public class ExceptionAdvice {

	@ExceptionHandler
	public ResponseEntity<ErrorMessage> handleException(Exception ex) {
		log.error("Unkown internal server error " + ex.getMessage());
		log.error("Exception class " + ex.getClass());
		log.error("Exception cause " + ex.getCause());

		return new ResponseEntity(new ErrorMessage("99999", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler
	public ResponseEntity<ErrorMessage> handleCommonException(CommonException exception) {
		log.error(String.format("Common error: %s %s %s", exception.getCode(), exception.getMessage(),
				exception.getStatus()));
		return new ResponseEntity(new ErrorMessage(exception.getCode(), exception.getMessage(), exception.getStatus()),
				exception.getStatus());
	}

	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleValidateException(ValidateException exception) {
		return new ResponseEntity(exception.getMessageMap(), exception.getStatus());
	}
}
