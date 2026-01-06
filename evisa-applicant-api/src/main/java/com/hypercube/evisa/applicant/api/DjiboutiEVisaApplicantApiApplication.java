package com.hypercube.evisa.applicant.api;

 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author SivaSreenivas
 *
 */
@SpringBootApplication
@ComponentScan({ "com.hypercube.evisa.applicant.api", "com.hypercube.evisa.common.api" })
@PropertySource("file:${external.config.location}")
public class DjiboutiEVisaApplicantApiApplication {

    /**
     * @param args
     */

	
    public static void main(String[] args) {
      SpringApplication.run(DjiboutiEVisaApplicantApiApplication.class, args);
    }
    

}