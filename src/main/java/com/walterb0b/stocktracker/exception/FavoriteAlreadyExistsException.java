package com.walterb0b.stocktracker.exception;

public class FavoriteAlreadyExistsException extends RuntimeException {
    public FavoriteAlreadyExistsException(String stockSymbol) {
        super("Favorite stock already exists: " + stockSymbol);
    }
}
