package com.alexnail.frtechassignment;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class FRTechAssignmentApplication {

    @Value("${spring.mvc.format.date}")
    private String datePattern;

    public static void main(String[] args) {
        SpringApplication.run(FRTechAssignmentApplication.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(LocalDate.class, String.class);
        modelMapper.addConverter(new AbstractConverter<LocalDate, String>() {
            @Override
            protected String convert(LocalDate date) {
                return date.format(DateTimeFormatter.ofPattern(datePattern));
            }
        });
        modelMapper.createTypeMap(String.class, LocalDate.class);
        modelMapper.addConverter(new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String s) {
                return LocalDate.parse(s, DateTimeFormatter.ofPattern(datePattern));
            }
        });
        return modelMapper;
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        return objectMapper;
    }
}
