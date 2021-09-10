package com.alexnail.frtechassignment.service;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;

import java.util.Collection;

public interface TransactionDataService {
    Collection<TransactionData> getAllTransactions();

    TransactionData getTransaction(TransactionId transactionId);

    Collection<TransactionData> saveTransactions(Collection<TransactionData> entities);
}
