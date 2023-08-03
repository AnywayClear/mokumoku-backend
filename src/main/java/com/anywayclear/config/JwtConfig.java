package com.anywayclear.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "jwtsecret")
@PropertySource(value = {"jwtsecret.properties"})
@Getter
@Setter
@ToString
public class JwtConfig {
    private String key;
    private String prefix;
    private String header;
    private String success;
    private String fail;
}
