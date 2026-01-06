/**
 * 
 */
package com.hypercube.evisa.applicant.api.configuration;

import java.util.Collections;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author SivaSreenivas
 *
 */
@Configuration
@EnableSwagger2
@Profile("default")
public class SwaggerConfig {
    
    /**
     * 
     */
    private static final String COMPANY_WEBSITE = "www.hypercube.dj";

    /**
     * 
     */
    public SwaggerConfig() {
        super();
    }

    /**
     * @return
     */
    @Bean
    Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select().build().apiInfo(apiInfo());
    }

    /**
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("Djibouti E-Visa", "Djibouti E-Visa Application", "1.0.0.RELEASE", COMPANY_WEBSITE,
                new springfox.documentation.service.Contact("Hypercube Solutions Private Limited", COMPANY_WEBSITE,
                        "contact@hypercube.dj"),
                "Terms of Use", COMPANY_WEBSITE, Collections.emptyList());
    }


}
