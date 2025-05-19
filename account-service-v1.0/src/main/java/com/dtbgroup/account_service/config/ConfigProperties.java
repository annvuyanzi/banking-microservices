package com.dtbgroup.account_service.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@ConfigurationProperties(prefix = "app.services")
@Data
public class ConfigProperties {
    private CustomerConfig customer;
    private CardConfig card;

    @Getter
    @Setter
    public static class CustomerConfig{
        public String baseUrl;
    }
    @Getter
    @Setter
    public static class CardConfig{
        private String baseUrl;
    }

}
