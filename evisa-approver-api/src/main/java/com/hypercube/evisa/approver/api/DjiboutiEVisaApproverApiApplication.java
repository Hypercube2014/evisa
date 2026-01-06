package com.hypercube.evisa.approver.api;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author SivaSreenivas
 *
 */
@SpringBootApplication
@ComponentScan({ "com.hypercube.evisa.approver.api", "com.hypercube.evisa.common.api" })
@PropertySource("file:${external.config.location}")
public class DjiboutiEVisaApproverApiApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
	   SpringApplication.run(DjiboutiEVisaApproverApiApplication.class, args);
		
	}

}
