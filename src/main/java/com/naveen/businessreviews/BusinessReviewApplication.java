package com.naveen.businessreviews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BusinessReviewApplication {

	public static final Logger logger = LoggerFactory.getLogger(BusinessReviewApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BusinessReviewApplication.class, args);
	}

}
