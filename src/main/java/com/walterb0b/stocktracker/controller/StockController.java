package com.walterb0b.stocktracker.controller;

import com.walterb0b.stocktracker.dto.DailyStockResponse;
import com.walterb0b.stocktracker.dto.FavoriteStockRequest;
import com.walterb0b.stocktracker.dto.StockOverviewResponse;
import com.walterb0b.stocktracker.dto.StockResponse;
import com.walterb0b.stocktracker.entity.FavoriteStock;
import com.walterb0b.stocktracker.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{stockSymbol}")
    public StockResponse getStockPrice(@PathVariable String stockSymbol) {
        return stockService.getStockForSymbol(stockSymbol.toUpperCase());
    }

    @GetMapping("/{stockSymbol}/overview")
    public StockOverviewResponse getStockOverview(@PathVariable String stockSymbol) {
        return stockService.getStockOverviewForSymbol(stockSymbol.toUpperCase());
    }

    @GetMapping("/{stockSymbol}/history")
    public List<DailyStockResponse> getStockHistory(
            @PathVariable String stockSymbol,
            @RequestParam(defaultValue = "30") int days
    ) {
        return stockService.getStockHistory(stockSymbol.toUpperCase(), days);
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteStock> saveFavoriteStock(@RequestBody FavoriteStockRequest request) {
        final FavoriteStock savedStock = stockService.addFavorite(request.getSymbol());
        return ResponseEntity.ok(savedStock);
    }

    @GetMapping("/favorites")
    public List<StockResponse> getFavoritesWithPrices() {
        return stockService.getFavoriteStocksWithLivePrices();
    }
}
