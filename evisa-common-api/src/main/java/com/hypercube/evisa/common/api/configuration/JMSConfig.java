/**
 * 
 */
package com.hypercube.evisa.common.api.configuration;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * @author SivaSreenivas
 *
 */
@Component
public class JMSConfig {

    /**
     * @param connectionFactory
     * @param configurer
     * @return
     */
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        /*
         * This provides all boot's default to this factory, including the
         * message converter
         */
        /* anonymous class */
        factory.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                System.err.println("An error has occurred in the transaction");
            }
        });
        /* lambda function */
        factory.setErrorHandler(t -> System.err.println("An error has occurred in the transaction"));

        configurer.configure(factory, connectionFactory);
        /* You could still override some of Boot's default if necessary. */
        return factory;
    }

    /**
     * @return
     */
    @Bean /* Serialize message content to json using TextMessage */
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
