package com.service.paymentprocessingservice.event;

import java.security.SecureRandom;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.service.commonservice.model.PaymentDTO;
import com.service.commonservice.utils.Constant;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class EventConsumer {

	Gson gson = new Gson();

	SecureRandom random = new SecureRandom();

	@Autowired
	private EventProducer eventProducer;

	public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_CREATED_TOPIC)))
				.receive().subscribe(this::paymentCcreated);
	}

	@SneakyThrows
	public void paymentCcreated(ReceiverRecord<String, String> receiverRecord) {
		log.info("PAYMENT_CREATED_TOPIC topics is received ");
		PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(), PaymentDTO.class);
		String[] rnadomStatus = { Constant.STATUS_PAYMENT_REJECTED, Constant.STATUS_PAYMENT_SUCCESSFUL };

		int index = random.nextInt(rnadomStatus.length);
		paymentDTO.setStatus(rnadomStatus[index]);
		Thread.sleep(3000);
		eventProducer.send(Constant.PAYMENT_COMPLETED_TOPIC, gson.toJson(paymentDTO)).subscribe();
		

	}

}
