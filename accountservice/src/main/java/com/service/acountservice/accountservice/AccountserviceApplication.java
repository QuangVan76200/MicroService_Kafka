package com.service.acountservice.accountservice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.format.datetime.DateFormatter;

@SpringBootApplication
@EnableR2dbcRepositories
@ComponentScan({"com.service.acountservice",  "com.service.commonservice"})
public class AccountserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountserviceApplication.class, args);
		
//		LocalDateTime newDate =  LocalDateTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//		String formatDate = newDate.format(formatter);
//		System.out.println(newDate);
	}

}
