package com.service.paymentprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.service.paymentprocessingservice", "com.service.commonservice"})
public class PaymentProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentProcessingServiceApplication.class, args);
	}

}
