package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
public class ITBase {

    private static final PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
        if (!postgres.isRunning()) {
            postgres.start();
        }
    }

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @AfterEach
    public void cleanUpEachTest() {
        databaseCleaner.cleanDatabase();
    }
}
