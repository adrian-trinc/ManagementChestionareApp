package com.management.chestionare.Controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ControllerConfiguration {
    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }
}
