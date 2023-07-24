package com.service.profileservice.event;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.service.commonservice.utils.Constant;
import com.service.profileservice.model.ProfileDTO;
import com.service.profileservice.service.ProfileService;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class EventConsumer {

	Gson gson = new Gson();

	@Autowired
	private ProfileService profileService;

	public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PROFILE_ONBOARDED_TOPIC)))
				.receive().subscribe(this::profileOnboarded);
	}

	public void profileOnboarded(ReceiverRecord<String, String> receiverRecord) {
		log.info("Profile Onboarded event");

		ProfileDTO proDto = gson.fromJson(receiverRecord.value(), ProfileDTO.class);
		profileService.updateStatusProfiles(proDto).subscribe();

	}

}
