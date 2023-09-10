package com.service.profileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@SpringBootApplication
@EnableR2dbcRepositories
@ComponentScan({"com.service.profileservice", "com.service.commonservice"})
//@OpenAPIDefinition(info = @Info(title = "APIs", version = "1.0", description = "Documentation Profile Service"))
public class ProfileserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileserviceApplication.class, args);
	}

}
