package com.alexnail.frtechassignment.service.impl;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.repository.TransactionDataRepository;
import com.alexnail.frtechassignment.service.ExternalSystemClient;
import com.alexnail.frtechassignment.service.TransactionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TransactionDataServiceImpl implements TransactionDataService {

    private static final String EXTERNAL_SYSTEM_MESSAGE_TEMPLATE =
            "TransactionData with id=%s already exists and has amount of %.2f. New amount of %.2f will be added to it.";
    @Autowired
    private TransactionDataRepository repository;
    @Autowired
    private ExternalSystemClient externalSystemClient;

    @Override
    public Collection<TransactionData> getAllTransactions() {
        return repository.findAll();
    }

    @Override
    public TransactionData getTransaction(TransactionId transactionId) {
        return repository.getById(transactionId);
    }

    @Override
    public Collection<TransactionData> saveTransactions(Collection<TransactionData> transactions) {
        return transactions.stream()
                .map(this::saveTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionData saveTransaction(TransactionData item) {
        TransactionId id = new TransactionId(item.getDate(), item.getType());
        TransactionData existing = repository.getById(id);
        if (existing != null) {
            externalSystemClient.sendMessage(
                    String.format(EXTERNAL_SYSTEM_MESSAGE_TEMPLATE, id, existing.getAmount(), item.getAmount()));
            item.setAmount(existing.getAmount() + item.getAmount());
        }
        return repository.save(item);
    }
}
