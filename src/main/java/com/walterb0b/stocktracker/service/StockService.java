package com.walterb0b.stocktracker.service;

import com.walterb0b.stocktracker.client.StockClient;
import com.walterb0b.stocktracker.dto.*;
import com.walterb0b.stocktracker.entity.FavoriteStock;
import com.walterb0b.stocktracker.exception.FavoriteAlreadyExistsException;
import com.walterb0b.stocktracker.repository.FavoriteStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class StockService {

    private final StockClient stockClient;
    private final FavoriteStockRepository favoriteStockRepository;

    public StockService(StockClient stockClient, FavoriteStockRepository favoriteStockRepository) {
        this.stockClient = stockClient;
        this.favoriteStockRepository = favoriteStockRepository;
    }

    @Cacheable(value = "stocks", key = "#stockSymbol")
    public StockResponse getStockForSymbol(String stockSymbol) {
        AlphaVantageResponse response = stockClient.getStockQuote(stockSymbol);

        // Tjek for null for at undgå NullPointerException
        if (response == null || response.globalQuote() == null || response.globalQuote().price() == null) {
            System.out.println("Failed to fetch: " + stockSymbol);
            // Fallback svar
            return StockResponse.builder()
                    .symbol(stockSymbol)
                    .price(0.0)
                    .lastUpdated("N/A")
                    .build();
        }

        return StockResponse.builder()
                .symbol(response.globalQuote().symbol())
                .price(Double.parseDouble(response.globalQuote().price()))
                .lastUpdated(response.globalQuote().latestTradingDay())
                .build();
    }

    public StockOverviewResponse getStockOverviewForSymbol(String stockSymbol) {
        return stockClient.getStockOverview(stockSymbol);
    }

    public List<DailyStockResponse> getStockHistory(String stockSymbol, int days) {
        StockHistoryResponse response = stockClient.getStockHistory(stockSymbol);

        return response.timeSeries().entrySet().stream()
                .limit(days)
                .map(entry -> {
                    var date = entry.getKey();
                    var dailyData = entry.getValue();
                    return new DailyStockResponse(
                            date,
                            Double.parseDouble(dailyData.open()),
                            Double.parseDouble(dailyData.high()),
                            Double.parseDouble(dailyData.low()),
                            Double.parseDouble(dailyData.close()),
                            Long.parseLong(dailyData.volume())
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteStock addFavorite(String stockSymbol) {
        if(favoriteStockRepository.existsBySymbol(stockSymbol)) {
            throw new FavoriteAlreadyExistsException(stockSymbol);
        }

        FavoriteStock favoriteStock = FavoriteStock.builder()
                .symbol(stockSymbol)
                .build();

        return favoriteStockRepository.save(favoriteStock);
    }

    public List<StockResponse> getFavoriteStocksWithLivePrices() {
        List<FavoriteStock> favorites = favoriteStockRepository.findAll();

        List<StockResponse> result = new ArrayList<>();

        for (FavoriteStock favorite : favorites) {
            try {
                StockResponse stock = getStockForSymbol(favorite.getSymbol());
                result.add(stock);

            } catch (Exception e) {
                System.out.println("Error fetching " + favorite.getSymbol() + ": " + e.getMessage());
            }
        }

        return result;
    }
}
