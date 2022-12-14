package com.github.kudr9tov.epaper.utils;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate unmarshal(String value) {
        return LocalDate.parse(value, FORMAT);
    }

    @Override
    public String marshal(LocalDate localDate) {
        return localDate.format(FORMAT);
    }
}
