package com.alexnail.frtechassignment.controller;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.service.TransactionDataService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.anyCollection;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionDataController.class)
class TransactionDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionDataService service;

    @SneakyThrows
    @Test
    void testGetAllTransactions() {
        TransactionData item1 = new TransactionData(LocalDate.of(2021, Month.SEPTEMBER, 10), "credit", 123.12);
        TransactionData item2 = new TransactionData(LocalDate.of(2021, Month.SEPTEMBER, 10), "debit", 123.12);
        List<TransactionData> transactions = Arrays.asList(item1, item2);
        given(service.getAllTransactions()).willReturn(transactions);

        mvc.perform(get("/api/transactiondata").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("10-09-2021")))
                .andExpect(jsonPath("$[0].type", is("credit")))
                .andExpect(jsonPath("$[0].amount", is("123.12")));
    }

    @SneakyThrows
    @Test
    void testGetTransactionByDate() {
        TransactionData item1 = new TransactionData(LocalDate.of(2021, Month.SEPTEMBER, 10), "credit", 123.12);
        given(service.getTransaction(item1.getId())).willReturn(item1);

        mvc.perform(get("/api/transactiondata/10-09-2021").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is("10-09-2021")))
                .andExpect(jsonPath("$.type", is("credit")))
                .andExpect(jsonPath("$.amount", is("123.12")));
    }

    @SneakyThrows
    @Test
    void testGetTransactionByDateAndTypeNotFound() {
        TransactionData item1 = new TransactionData(LocalDate.of(2021, Month.SEPTEMBER, 10), "credit", 123.12);
        given(service.getTransaction(item1.getId())).willReturn(item1);

        mvc.perform(get("/api/transactiondata/10-09-2021?type=debit").contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testGetTransactionByDateAndType() {
        TransactionData item1 = new TransactionData(LocalDate.of(2021, Month.SEPTEMBER, 10), "credit", 123.12);
        given(service.getTransaction(item1.getId())).willReturn(item1);

        mvc.perform(get("/api/transactiondata/10-09-2021?type=credit").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is("10-09-2021")))
                .andExpect(jsonPath("$.type", is("credit")))
                .andExpect(jsonPath("$.amount", is("123.12")));
    }

    @SneakyThrows
    @Test
    void testAddTransaction() {
        TransactionData item1 = new TransactionData(LocalDate.of(2021, Month.SEPTEMBER, 10), "debit", 456.78);
        given(service.saveTransactions(anyCollection())).willReturn(List.of(item1));

        mvc.perform(post("/api/transactiondata")
                        .contentType(APPLICATION_JSON)
                        .content("[{" +
                                "\"date\": \"10-09-2021\"," +
                                "\"type\": \"debit\"," +
                                "\"amount\": \"456.78\"" +
                                "}]")
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.[0].date", is("10-09-2021")))
                .andExpect(jsonPath("$.[0].type", is("debit")))
                .andExpect(jsonPath("$.[0].amount", is("456.78")));
    }
}