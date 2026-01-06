/**
 * 
 */
package com.hypercube.evisa.common.api.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SivaSreenivas
 *
 */
@Configuration
public class GeneralConfig {
    
    /**
     * @return
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
