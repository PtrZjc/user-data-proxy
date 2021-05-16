package pl.zajacp.proxy.infrastructure.api;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ApiConfig {

    @Bean
    public RestTemplate restTemplate(ExternalPathProperties externalPath) {
        return new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(externalPath.getBaseUrl()))
                .build();
    }
}
