package pl.zajacp.proxy.test.config;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@TestConfiguration
public class WiremockConfig {

    @Bean
    @Primary
    public WireMockConfiguration wireMockConfiguration(Environment environment,
                                                       @Value("${wiremock.base-folder}") String wiremockBaseFolder) {
        WireMockConfiguration config = new WireMockConfiguration();
        config.extensions(new ResponseTemplateTransformer(true));
        config.usingFilesUnderDirectory(wiremockBaseFolder);
        config.port(Integer.parseInt(environment.getProperty("wiremock.server.port")));
        return config;
    }
}