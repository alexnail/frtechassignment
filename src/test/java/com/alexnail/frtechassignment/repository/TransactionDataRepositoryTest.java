package com.alexnail.frtechassignment.repository;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.repository.impl.TransactionDataRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private static final LocalDate DATE = LocalDate.of(2021, Month.SEPTEMBER, 11);
    @Autowired
    private TransactionDataRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(new TransactionData(DATE, "credit", 123.45));
        repository.save(new TransactionData(DATE, "debit", 456.78));
    }

    @Test
    void testSave() {
        LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 12);
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
    void testSaveWithExistingTransactionId() {
        TransactionData saved = repository.save(new TransactionData(DATE, "credit", 123.45));
        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals(DATE, saved.getDate()),
                () -> assertEquals("credit", saved.getType()),
                () -> assertEquals(123.45, saved.getAmount())
        );

        Set<TransactionData> all = repository.findAll();
        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals(2, all.size())
        );
    }

    @Test
    void testFindAll() {
        Set<TransactionData> all = repository.findAll();
        assertAll(
                () -> assertNotNull(all),
                () -> assertEquals(2, all.size()),
                () -> all.forEach(Assertions::assertNotNull)
        );
    }

    @Test
    void testGetById() {
        TransactionData credit = repository.getById(new TransactionId(DATE, "credit"));
        assertAll(
                () -> assertNotNull(credit),
                () -> assertEquals("credit", credit.getType()),
                () -> assertEquals(123.45, credit.getAmount())
        );

        TransactionData debit = repository.getById(new TransactionId(DATE, "debit"));
        assertAll(
                () -> assertNotNull(debit),
                () -> assertEquals("debit", debit.getType()),
                () -> assertEquals(456.78, debit.getAmount())
        );
    }

    @Test
    void testDeleteAll() {
        Set<TransactionData> deleted = repository.deleteAll();
        assertEquals(2, deleted.size());
        Set<TransactionData> all = repository.findAll();
        assertEquals(0, all.size());
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