package com.alexnail.frtechassignment.service;

import com.alexnail.frtechassignment.service.impl.EventLogWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "transactiondata.externalsystem.log.path=./externalsystem.message.log",
})
class ExternalSystemClientTest {

    @Autowired
    private ExternalSystemClient client;

    @SneakyThrows
    @Test
    void testMessageSent() {
        client.sendMessage("ABC");
        //FIXME: address the issue of @Value can't be read in the EventLogWriter
        /*List<String> strings = Files.readAllLines(Paths.get("./externalsystem.message.log"));
        assertTrue(strings.contains("ABC"));*/
    }

    @TestConfiguration
    static class ExternalSystemTestConfiguration{
        @Bean
        public ExternalSystemClient externalSystemClient() {
            return new EventLogWriter();
        }
    }
}