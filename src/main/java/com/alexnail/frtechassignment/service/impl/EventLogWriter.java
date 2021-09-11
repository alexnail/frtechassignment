package com.alexnail.frtechassignment.service.impl;

import com.alexnail.frtechassignment.service.ExternalSystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

@Service
@Slf4j
public class EventLogWriter implements ExternalSystemClient {

    @Value("${transactiondata.externalsystem.log.path}")
    private String logPath;

    @Override
    public void sendMessage(String message) {
        try {
            Files.writeString(Paths.get(logPath), String.format("%s%n",message), StandardCharsets.UTF_8, CREATE, WRITE, APPEND);
        } catch (IOException e) {
            log.error(String.format("Failed to add the message %s to the event log.", message),e);
        }
    }
}
