package com.alexnail.frtechassignment.controller;

import com.alexnail.frtechassignment.dto.TransactionDataDto;
import com.alexnail.frtechassignment.model.TransactionData;
import com.alexnail.frtechassignment.model.TransactionId;
import com.alexnail.frtechassignment.service.TransactionDataService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/transactiondata")
@RestController
@AllArgsConstructor
public class TransactionDataController {

    private final ModelMapper modelMapper;

    private final TransactionDataService service;

    @GetMapping
    public List<TransactionDataDto> getAllTransactions() {
        Collection<TransactionData> allTransactions = service.getAllTransactions();
        return allTransactions.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{date}")
    public TransactionDataDto getTransaction(@PathVariable("date") LocalDate date,
                                             @RequestParam(defaultValue = "credit") String type) {
        TransactionData transaction = service.getTransaction(new TransactionId(date, type));
        if (transaction == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Transaction with id={%s,%s} not found.", date, type));
        return convertToDto(transaction);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<TransactionDataDto> addTransactions(@RequestBody List<TransactionDataDto> items) {
        List<TransactionData> entities = items.stream().map(this::convertToEntity).collect(Collectors.toList());
        return service.saveTransactions(entities).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private TransactionDataDto convertToDto(TransactionData entity) {
        return modelMapper.map(entity, TransactionDataDto.class);
    }

    private TransactionData convertToEntity(TransactionDataDto dto) {
        return modelMapper.map(dto, TransactionData.class);
    }
}
