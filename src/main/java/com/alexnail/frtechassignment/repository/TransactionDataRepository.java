package com.alexnail.frtechassignment.repository;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;

import java.util.List;

public interface TransactionDataRepository {
    TransactionData save(TransactionData item);
    TransactionData getById(TransactionId item);
    List<TransactionData> findAll();
}
