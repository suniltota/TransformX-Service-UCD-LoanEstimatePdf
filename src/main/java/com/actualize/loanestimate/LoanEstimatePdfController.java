package com.actualize.loanestimate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
/**
 * This class initiates the current application
 * @author sboragala
 *
 */
@SpringBootApplication(scanBasePackages = "com.actualize.loanestimate")
@ImportResource("classpath:config.xml")
public class LoanEstimatePdfController extends SpringBootServletInitializer  {
	
	private static final Logger LOG = LogManager.getLogger(LoanEstimatePdfController.class);

	public static void main(String[] args) {
		SpringApplication.run(LoanEstimatePdfController.class, args);
	}

}
