package com.service.acountservice.accountservice.event;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.service.acountservice.accountservice.model.AccountDTO;
import com.service.acountservice.accountservice.service.AccountService;
import com.service.acountservice.accountservice.utils.DateUtils;
import com.service.acountservice.accountservice.utils.LocalDateTimeAdapter;
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
	        	System.out.println("New Account Success");
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
		PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(), PaymentDTO.class);
		accountService.bookAmount(paymentDTO.getAmount(), paymentDTO.getAccountId()).subscribe();
		
	}

}
