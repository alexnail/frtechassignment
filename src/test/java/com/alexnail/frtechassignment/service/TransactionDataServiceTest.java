package com.alexnail.frtechassignment.service;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.repository.TransactionDataRepository;
import com.alexnail.frtechassignment.service.impl.TransactionDataServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
class TransactionDataServiceTest {

    @Autowired
    private TransactionDataService service;

    @MockBean
    private TransactionDataRepository repository;

    @MockBean
    private ExternalSystemClient externalSystemClient;

    @Test
    void testGetAllTransactions() {
        LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 10);
        when(repository.findAll())
                .thenReturn(List.of(
                        new TransactionData(date, "credit", 123.45),
                        new TransactionData(date, "debit", 456.78)
                ));

        Collection<TransactionData> transactions = service.getAllTransactions();
        assertAll(
                () -> assertNotNull(transactions),
                () -> assertFalse(transactions.isEmpty()),
                () -> assertEquals(2, transactions.size())
        );
    }

    @Test
    void testGetTransaction() {
        LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 10);
        when(repository.getById(any(TransactionId.class)))
                .thenReturn(new TransactionData(date, "credit", 123.45));

        TransactionData transaction = service.getTransaction(new TransactionId(date, "credit"));
        assertAll(
                () -> assertNotNull(transaction),
                () -> assertEquals(date, transaction.getDate()),
                () -> assertEquals("credit", transaction.getType()),
                () -> assertEquals(123.45, transaction.getAmount())
        );
    }

    @Test
    void testSaveTransactions() {
        when(repository
                .save(any(TransactionData.class)))
                .thenAnswer((Answer<TransactionData>) invocation -> invocation.getArgument(0));

        LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 10);
        TransactionData data1 = new TransactionData(date, "credit", 123.45);
        TransactionData data2 = new TransactionData(date, "debit", 126.55);
        Collection<TransactionData> created = service.saveTransactions(List.of(data1, data2));
        assertAll(
                () -> assertNotNull(created),
                () -> assertEquals(2, created.size()),
                () -> created.forEach(Assertions::assertNotNull)
        );
    }

    @Test
    void testSaveTransactionWithExistingIdSumsAmounts() {
        LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 10);
        TransactionData data1 = new TransactionData(date, "credit", 123.45);

        when(repository
                .save(any(TransactionData.class)))
                .thenAnswer((Answer<TransactionData>) invocation -> invocation.getArgument(0));

        when(repository.getById(any(TransactionId.class)))
                .thenReturn(null)
                .thenReturn(data1);

        service.saveTransaction(data1);

        TransactionData saved = service.saveTransaction(new TransactionData(date, "credit", 126.55));
        assertEquals(250.00, saved.getAmount());

        verify(externalSystemClient)
                .sendMessage("TransactionData with id=TransactionId(date=2021-09-10, type=credit) already exists and has amount of 123.45. New amount of 126.55 will be added to it.");
    }

    @TestConfiguration
    static class TransactionDataServiceTestConfiguration {
        @Bean
        public TransactionDataService transactionDataService() {
            return new TransactionDataServiceImpl();
        }
    }

}