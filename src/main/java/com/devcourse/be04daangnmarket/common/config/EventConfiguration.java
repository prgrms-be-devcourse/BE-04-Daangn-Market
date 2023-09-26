package com.devcourse.be04daangnmarket.common.config;

import com.devcourse.be04daangnmarket.common.image.ImageEvents;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {
    private final ApplicationContext applicationContext;

    public EventConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public InitializingBean eventsInitializer() {
        return () -> ImageEvents.setPublisher(applicationContext);
    }
}
