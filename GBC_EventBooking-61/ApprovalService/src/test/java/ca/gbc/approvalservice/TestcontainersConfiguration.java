package ca.gbc.approvalservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestcontainersConfiguration {

    @Bean
    public MongoDBContainer mongoDBContainer() {
        MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");
        mongoDBContainer.start();
        System.setProperty("DB_URI", mongoDBContainer.getReplicaSetUrl());
        return mongoDBContainer;
    }

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        postgreSQLContainer.start();
        System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
        return postgreSQLContainer;
    }

    @Bean
    public GenericContainer<?> customServiceContainer() {
        GenericContainer<?> serviceContainer = new GenericContainer<>("my-service:latest")
                .withExposedPorts(8080);
        serviceContainer.start();
        System.setProperty("SERVICE_URL", "http://localhost:" + serviceContainer.getMappedPort(8080));
        return serviceContainer;
    }
}
