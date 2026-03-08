# BasicStockTracker

A simple Spring Boot project for tracking stock prices using the Alpha Vantage API. The project fetches live stock prices for favorites, stores them in an H2 database, and organizes code into controller, service, and client layers.

## Features

- Fetch live stock prices via **Alpha Vantage API**
- Cache API responses to avoid rate limits
- Store favorite stocks locally in an H2 database
- REST API to retrieve favorites with their prices

## Technologies

- Java 17
- Spring Boot
  - Spring Web
  - Spring Data JPA
  - Spring WebFlux (WebClient)
  - Spring Cache
- H2 Database
- Alpha Vantage API

## Getting Started

### 1. Clone the project

```bash
git clone https://github.com/Walterb0b/BasicStockTracker.git
cd BasicStockTracker
```
### 2. Add your Alpha Vantage API key

#### In src/main/resources/application.properties:

alpha.vantage.base.url=https://www.alphavantage.co/query

alpha.vantage.api.key=YOUR_API_KEY_HERE

You can get a free API key from Alpha Vantage: https://www.alphavantage.co/support/#api-key

### 3. Run the application
./mvnw spring-boot:run

Or run it from your IDE like any Spring Boot application.

### 4. Test the API

GET /favorites → Returns all favorite stocks with live prices

POST /favorites → Add a new favorite (depending on your controller setup)

### 5. H2 Database Console

If using in-memory H2, access the console here:

http://localhost:8080/h2-console

JDBC URL:

jdbc:h2:mem:stocktracker

## Design & Structure

### The project is organized into:

* Controller – REST endpoints for client requests

* Service – Business logic, caching, and error handling

* Client – WebClient calls to Alpha Vantage

* Repository/Entity – H2 database for favorite stocks

# Caching

Caching is used to minimize API calls, since Alpha Vantage free accounts have strict rate limits. Once a stock symbol is cached, repeated calls for the same symbol will use the cached data.

# Rate Limits

Alpha Vantage free accounts are typically limited to 5 requests per minute. With many favorites, only the first may return real data. Caching or throttling is required to avoid hitting limits.

### Future Improvements

Async WebClient with rate limiting

Additional endpoints (historical data, charts)

File-mode persistence instead of in-memory

Unit and integration tests

### License

This project is open source and free to use or extend.
