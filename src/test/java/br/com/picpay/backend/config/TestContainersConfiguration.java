package br.com.picpay.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@ActiveProfiles("test")
public class TestContainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresDbContainer(@Value("${spring.datasource.username}") String username,
                                               @Value("${spring.datasource.password}") String password,
                                               @Value("${spring.datasource.url}") String url) {

        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName(url.substring(url.lastIndexOf("/") + 1))
                .withUsername(username)
                .withPassword(password)
                .withExposedPorts(5432)
                .withReuse(false);
    }
}
