package com.alexnail.frtechassignment.service.impl;

import com.alexnail.frtechassignment.service.ExternalSystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventLogWriter implements ExternalSystemClient {

    //FIXME: address the issue of @Value can't be read
    /*@Value("${transactiondata.externalsystem.log.path}")
    private static String LOG_PATH;*/

    @Override
    public void sendMessage(String message) {
        /*try {
            Files.writeString(Paths.get(LOG_PATH), message, StandardCharsets.UTF_8, CREATE, WRITE, APPEND);
        } catch (IOException e) {
            log.error(String.format("Failed to add the message %s to the event log.", message),e);
        }*/
        log.info(message);
    }
}
