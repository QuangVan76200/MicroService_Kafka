package com.service.acountservice.accountservice.event;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.service.acountservice.accountservice.model.AccountDTO;
import com.service.acountservice.accountservice.service.AccountService;
import com.service.acountservice.accountservice.utils.DateUtils;
import com.service.acountservice.accountservice.utils.LocalDateTimeAdapter;
import com.service.commonservice.common.CommonException;
import com.service.commonservice.model.PaymentDTO;
import com.service.commonservice.model.ProfileDTO;
import com.service.commonservice.utils.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class EventConsumer {

	Gson gson = new GsonBuilder()
	        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
	        .create();

	@Autowired
	private AccountService accountService;

	@Autowired
	private EventProducer eventProducer;

	@Autowired
	private DateUtils dateUtils;

	public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PROFILE_ONBOARDING_TOPIC)))
				.receive().subscribe(this::profileOnboarding);
		
		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_REQUEST_TOPIC)))
		.receive().subscribe(this::paymentRequest);
		
		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_COMPLETED_TOPIC)))
		.receive().subscribe(this::paymentCompleted);
	}

	public void profileOnboarding(ReceiverRecord<String, String> receiverRecord) {
		log.info("Profile Onboarding event " + receiverRecord.value());

		ProfileDTO profileDTO = gson.fromJson(receiverRecord.value(), ProfileDTO.class);
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail(profileDTO.getEmail());
		accountDTO.setReserved(0);
		accountDTO.setUsername(profileDTO.getUsername());
		accountDTO.setPassword(profileDTO.getPassword());
		accountDTO.setBalance(profileDTO.getInitialBalance());
		accountDTO.setDateopened(dateUtils.convertDateToLocalDateTime(new Date()));
		accountDTO.setCurrency("USD");
		accountDTO.setEnabled(true);
		accountDTO.setRole(profileDTO.getRole());

		log.info("print accountDTO " + accountDTO.toString());

	    accountService.createAccount(accountDTO)
	        .doOnSuccess(createdAccount -> {
	        	log.info("New Account Success");
	        	profileDTO.setStatus(Constant.STATUS_PROFILE_ACTIVE);
	        	log.info("Send successful account creation event to complete the Saga process");
	            eventProducer.send(Constant.PROFILE_ONBOARDED_TOPIC, gson.toJson(profileDTO)).subscribe();
	        })
	        .doOnError(ex -> {
				System.out.println("New Account Failed");
				log.info("Handle error when creating new Account and perform revert by sending revert event");
				log.error(ex.getMessage());
				eventProducer.send(Constant.PROFILE_CREATION_FAILED_TOPIC, gson.toJson(profileDTO)).subscribe();
			})
	        .subscribe();

	}
	
	public void paymentRequest(ReceiverRecord<String, String> receiverRecord) {
		log.info("Received Message {PAYMENT_REQUEST_TOPIC} ");
		PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(), PaymentDTO.class);
		accountService.bookAmount(paymentDTO.getAmount(), paymentDTO.getAccountId()).subscribe(result -> {
			if(result.valueOf(true)) {
				paymentDTO.setStatus(Constant.STATUS_PAYMENT_PROCESSING);
				eventProducer.send(Constant.PAYMENT_CREATED_TOPIC, gson.toJson(paymentDTO)).subscribe();
			}
			else {
				throw new CommonException("A02", "Balance is not enoungh", HttpStatus.BAD_REQUEST);
			}
		});
		
	}
	
	public void paymentCompleted(ReceiverRecord<String, String> receiverRecord) {
		log.info("Received Message {PAYMENT_COMPLETED_TOPIC} ");
		PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(), PaymentDTO.class);
		
		if( Objects.equals(paymentDTO.getStatus(), Constant.STATUS_PAYMENT_SUCCESSFUL )) {
			accountService.subtract(paymentDTO.getAmount(), paymentDTO.getAccountId()).subscribe();		
		}
		else {
			accountService.rollbackReserved(paymentDTO.getAmount(), paymentDTO.getAccountId()).subscribe();
		}
	}

}
