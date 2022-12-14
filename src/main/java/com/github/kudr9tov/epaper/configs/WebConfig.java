package com.github.kudr9tov.epaper.configs;

import com.github.kudr9tov.epaper.utils.LocalDateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.Formatter;

import java.time.LocalDateTime;

@Configuration
public class WebConfig {
    @Bean
    @Primary
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new LocalDateTimeFormatter();
    }
}
