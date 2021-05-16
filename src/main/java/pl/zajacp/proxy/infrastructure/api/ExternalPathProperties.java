package pl.zajacp.proxy.infrastructure.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "api.ext")
@Setter
public class ExternalPathProperties {
    private String baseUrl;
    private String usersResource;
}