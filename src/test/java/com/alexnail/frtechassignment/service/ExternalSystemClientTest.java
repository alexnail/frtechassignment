package com.alexnail.frtechassignment.service;

import com.alexnail.frtechassignment.service.impl.EventLogWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "transactiondata.externalsystem.log.path=src/test/resources/externalsystem.event.log",
})
class ExternalSystemClientTest {

    @Autowired
    private ExternalSystemClient client;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Files.deleteIfExists(Paths.get("src/test/resources/externalsystem.event.log"));
    }

    @SneakyThrows
    @AfterAll
    static void afterAll() {
        Files.deleteIfExists(Paths.get("src/test/resources/externalsystem.event.log"));
    }

    @SneakyThrows
    @Test
    void testMessageSent() {
        client.sendMessage("ABC");

        List<String> strings = Files.readAllLines(Paths.get("src/test/resources/externalsystem.event.log"));
        assertTrue(strings.contains("ABC"));
    }

    @TestConfiguration
    static class ExternalSystemTestConfiguration{
        @Bean
        public ExternalSystemClient externalSystemClient() {
            return new EventLogWriter();
        }
    }
}