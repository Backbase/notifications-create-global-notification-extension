package com.backbase.dbs.presentation.services.notifications.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ValidatorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorConfig.class);

    @Bean("serviceApiValidator")
    @Primary
    public NotificationValidator notificationValidator() {
        LOGGER.info("NotificationValidator is used as a serviceApiValidator.");
        return new NotificationValidator();
    }
}
