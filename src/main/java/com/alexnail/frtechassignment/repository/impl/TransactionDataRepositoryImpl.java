package com.alexnail.frtechassignment.repository.impl;

import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.repository.TransactionDataRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Repository
@Slf4j
public class TransactionDataRepositoryImpl implements TransactionDataRepository {

    @Value("${transactiondata.storage.file.path}")
    private String storagePath;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public TransactionData save(TransactionData item) {
        Set<TransactionData> all = findAll();
        all.add(item);
        try {
            objectMapper.writeValue(new File(storagePath), all);
            return item;
        } catch (IOException e) {
            log.error(String.format("Failed to save %s to %s", item, storagePath), e);
            return null;
        }
    }

    @Override
    public TransactionData getById(TransactionId id) {
        return findAll().stream()
                .filter(i -> Objects.equals(i.getDate(), id.getDate()) && Objects.equals(i.getType(), id.getType()))
                .peek(System.out::println)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<TransactionData> findAll() {
        try {
            return objectMapper.readValue(new File(storagePath), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error(String.format("Failed to read TransactionData list from %s", storagePath), e);
        }
        return Set.of();
    }
}
