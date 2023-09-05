package com.service.paymentservice.event;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.service.commonservice.utils.Constant;
import com.service.paymentservice.model.PaymentDTO;
import com.service.paymentservice.service.PaymentService;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class EventConsumer {

	Gson gson = new Gson();

	@Autowired
	private PaymentService paymentService;

	public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_CREATED_TOPIC)))
				.receive().subscribe(this::paymentCreated);

		KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_COMPLETED_TOPIC)))
				.receive().subscribe(this::paymentCompleted);
	}

	public void paymentCreated(ReceiverRecord<String, String> receiverRecord) {
		log.info("Payment Created Event");
		PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(), PaymentDTO.class);
		paymentService.updateStatusPayment(paymentDTO).subscribe(result -> log.info("Update sttaus " + result));
	}

	public void paymentCompleted(ReceiverRecord<String, String> receiverRecord) {
		log.info("Payment Completed Event");
		PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(), PaymentDTO.class);
		paymentService.updateStatusPayment(paymentDTO).subscribe(result -> log.info("End Process Payment " + result));
	}

}
