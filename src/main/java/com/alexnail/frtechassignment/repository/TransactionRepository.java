package com.alexnail.frtechassignment.repository;

import com.alexnail.frtechassignment.model.TransactionData;

import java.util.List;

public interface TransactionRepository {
    TransactionData save(TransactionData item);
    TransactionData getById(TransactionData item);
    List<TransactionData> findAll();
}
