package com.alexnail.frtechassignment.repository;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;

import java.util.Set;

public interface TransactionDataRepository {
    TransactionData save(TransactionData item);
    TransactionData getById(TransactionId item);
    Set<TransactionData> findAll();
    Set<TransactionData> deleteAll();
}
