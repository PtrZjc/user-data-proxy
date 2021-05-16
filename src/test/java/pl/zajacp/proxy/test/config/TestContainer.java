package pl.zajacp.proxy.test.config;

import lombok.Getter;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainer {

    private final static String POSTGRES_IMAGE_VERSION = "postgres:9.6.22-alpine";

    @Getter
    private final static PostgreSQLContainer instance = PostgresContainer.INSTANCE.getInstance();

    private enum PostgresContainer {
        INSTANCE(new PostgreSQLContainer(POSTGRES_IMAGE_VERSION));

        PostgresContainer(PostgreSQLContainer instance) {
            this.instance = instance;
            instance.start();
        }

        @Getter
        private final PostgreSQLContainer instance;
    }

    public static class TestContainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + instance.getJdbcUrl(),
                    "spring.datasource.username=" + instance.getUsername(),
                    "spring.datasource.password=" + instance.getPassword(),
                    "spring.datasource.driver-class-name=org.postgresql.Driver",
                    "spring.liquibase.default-schema=user_proxy",
                    "spring.liquibase.change-log=classpath:/db/changelog.xml"
            );
            values.applyTo(configurableApplicationContext);
        }
    }
}
