package com.alexnail.frtechassignment;

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
        return modelMapper;
    }
}
