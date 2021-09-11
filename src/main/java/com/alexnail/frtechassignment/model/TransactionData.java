package com.alexnail.frtechassignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {

    //Hardcoding the pattern here isn't the nicest solution but there's no other option to set it from config -> annotations are being processed at compile time
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    private String type;

    private Double amount; // Double is used here for simplicity purpose, in prod I'd go with BigDecimal

    @JsonIgnore
    public TransactionId getId() {
        return new TransactionId(date, type);
    }
}
