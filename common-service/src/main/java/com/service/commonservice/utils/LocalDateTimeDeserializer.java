package com.service.commonservice.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.util.StdConverter;

public class LocalDateTimeDeserializer extends StdConverter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String value) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize LocalDateTime: " + e.getMessage(), e);
        }
    }
}






