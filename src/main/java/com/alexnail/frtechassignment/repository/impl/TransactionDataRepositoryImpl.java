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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Repository
@Slf4j
public class TransactionDataRepositoryImpl implements TransactionDataRepository {

    @Value("${transactiondata.storage.file.path}")
    private String storagePath;
    private File storage;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        storage = new File(storagePath);
    }

    @Override
    public TransactionData save(TransactionData item) {
        Set<TransactionData> all = findAll();
        all.add(item);
        try {
            objectMapper.writeValue(storage, all);
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
            return objectMapper.readValue(storage, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error(String.format("Failed to read TransactionData set from %s", storagePath), e);
        }
        return Set.of();
    }

    @Override
    public Set<TransactionData> deleteAll() {
        Set<TransactionData> all = findAll();
        try {
            objectMapper.writeValue(storage, Set.of());
            return all;
        } catch (IOException e) {
            log.error(String.format("Failed to delete TransactionData set from %s", storagePath), e);
            return Set.of();
        }
    }
}
