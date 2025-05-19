package com.dtbgroup.card_service.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.services")
@Data
public class ConfigProperties {
    private AccountConfig account;
    private String bin;

    @Getter
    @Setter
    public static class AccountConfig{
        public String baseUrl;
    }

}
