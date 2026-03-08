package com.walterb0b.stocktracker.dto;

import lombok.Builder;

@Builder
public record StockResponse (
        String symbol,
        double price,
        String lastUpdated
) {}
