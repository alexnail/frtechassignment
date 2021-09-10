package com.alexnail.frtechassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionId implements Serializable {
    private LocalDate date;
    private String type;
}
