package com.actualize.loanestimate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = "com.actualize.loanestimate")
@ImportResource("classpath:config.xml")
public class LoanEstimatePdfController extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(LoanEstimatePdfController.class, args);
	}

}
