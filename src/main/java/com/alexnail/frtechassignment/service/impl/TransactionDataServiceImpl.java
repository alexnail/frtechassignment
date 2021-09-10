package com.alexnail.frtechassignment.service.impl;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.service.TransactionDataService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransactionDataServiceImpl implements TransactionDataService {

    @Override
    public Collection<TransactionData> getAllTransactions() {
        return null;
    }

    @Override
    public TransactionData getTransaction(TransactionId transactionId) {
        return null;
    }

    @Override
    public Collection<TransactionData> saveTransactions(Collection<TransactionData> entities) {
        return null;
    }
}
