package com.alexnail.frtechassignment.repository;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.repository.impl.TransactionDataRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "transactiondata.storage.file.path=src/test/resources/transactions.json",
})
class TransactionDataRepositoryTest {

    @Autowired
    private TransactionDataRepository repository;

    @Test
    void testSave() {
        LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 11);
        TransactionData saved = repository.save(new TransactionData(date, "credit", 123.45));

        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals(date, saved.getDate()),
                () -> assertEquals("credit", saved.getType()),
                () -> assertEquals(123.45, saved.getAmount())
        );

        Set<TransactionData> all = repository.findAll();
        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals(3, all.size())
        );
    }

    @Test
    void testFindAll() {
        Set<TransactionData> all = repository.findAll();
        assertAll(
                () -> assertNotNull(all),
                () -> assertFalse(all.isEmpty()),
                () -> all.forEach(Assertions::assertNotNull)
        );
    }

    @Test
    void testGetById() {
        LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 10);
        TransactionData credit = repository.getById(new TransactionId(date, "credit"));
        assertAll(
                () -> assertNotNull(credit)
        );
    }

    @TestConfiguration
    static class TransactionDataRepositoryTestConfiguration {
        @Bean
        public TransactionDataRepository dataRepository() {
            return new TransactionDataRepositoryImpl();
        }

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper;
        }
    }
}