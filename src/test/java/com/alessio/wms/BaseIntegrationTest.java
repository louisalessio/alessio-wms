package com.alessio.wms;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseIntegrationTest {
    
    @Container
    @ServiceConnection
    protected static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:18-alpine");
}