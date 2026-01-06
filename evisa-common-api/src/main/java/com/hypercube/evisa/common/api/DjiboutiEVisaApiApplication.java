package com.hypercube.evisa.common.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;



/**
 * @author SivaSreenivas
 *
 */
@SpringBootApplication
@PropertySource("file:${common.config.location}")
public class DjiboutiEVisaApiApplication {

	/**
	 * @param args
	 */

	
	public static void main(String[] args) {
		SpringApplication.run(DjiboutiEVisaApiApplication.class, args);
	}

		
	
}
