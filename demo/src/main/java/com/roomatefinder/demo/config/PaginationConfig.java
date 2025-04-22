package com.roomatefinder.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PaginationConfig {

    @Value("${app.pagination.default-page-size:6}")
    private int defaultPageSize;

    @Value("${app.pagination.max-page-size:20}")
    private int maxPageSize;

    public int validatePageSize(Integer requestedSize) {
        if (requestedSize == null || requestedSize <= 0) {
            return defaultPageSize;
        }
        return Math.min(requestedSize, maxPageSize);
    }
}