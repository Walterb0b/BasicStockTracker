package com.walterb0b.stocktracker.repository;

import com.walterb0b.stocktracker.entity.FavoriteStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteStockRepository extends JpaRepository<FavoriteStock, Long> {
    boolean existsBySymbol(String symbol);

}
