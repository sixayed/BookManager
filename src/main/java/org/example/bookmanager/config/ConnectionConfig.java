package org.example.bookmanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class ConnectionConfig {
    private String hostname;
    private String username;
    private String password;
    private int port;
}
