package com.alexnail.frtechassignment.repository.impl;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.repository.TransactionDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionDataRepositoryImpl implements TransactionDataRepository {
    @Override
    public TransactionData save(TransactionData item) {
        return null;
    }

    @Override
    public TransactionData getById(TransactionId item) {
        return null;
    }

    @Override
    public List<TransactionData> findAll() {
        return null;
    }
}
