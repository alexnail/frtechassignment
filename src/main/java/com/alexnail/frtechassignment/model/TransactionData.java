package com.alexnail.frtechassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
//@Entity
//@IdClass(TransactionId.class)
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {

    //@Id
    private LocalDate date;
    //@Id
    private String type;

    // Double is used here for simplicity purpose, in prod I'd go with BigDecimal
    private Double amount;
}
