package com.service.acountservice.accountservice.event;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.service.acountservice.accountservice.model.AccountDTO;
import com.service.acountservice.accountservice.service.AccountService;
import com.service.acountservice.accountservice.utils.DateUtils;
import com.service.acountservice.accountservice.utils.PBKDF2Encoder;
import com.service.commonservice.model.ProfileDTO;
import com.service.commonservice.utils.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class EventConsumer {

	Gson gson = new Gson();

	@Autowired
	private AccountService accountService;

	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	private DateUtils dateUtils;
	
	

	public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PROFILE_ONBOARDING_TOPIC)))
				.receive().subscribe(this::profileOnboarding);
	}

	public void profileOnboarding(ReceiverRecord<String, String> receiverRecord) {
		System.out.println("print to Testinh" + receiverRecord.value());
		
		
		log.info("Profile Onboarding event");
		ProfileDTO profileDTO = gson.fromJson(receiverRecord.value(), ProfileDTO.class);
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setEmail(profileDTO.getEmail());
		accountDTO.setReserved(0);
		accountDTO.setUsername(profileDTO.getUsername());
		accountDTO.setPassword(profileDTO.getPassword());
		accountDTO.setBalance(profileDTO.getInitialBalance());
		accountDTO.setDateopened(dateUtils.convertDateToLocalDateTime(new Date()));
		accountDTO.setCurrency("USD");
		accountDTO.setRole(profileDTO.getRole());

		log.info("print accountDTO " + accountDTO.toString());
		accountService.createAccount(accountDTO).subscribe(res -> {
			profileDTO.setStatus(Constant.STATUS_PROFILE_ACTIVE);
			eventProducer.send(Constant.PROFILE_ONBOARDED_TOPIC, gson.toJson(profileDTO)).subscribe();
		});

	}

}
