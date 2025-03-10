DROP TABLE IF EXISTS security; -- Reset the table
CREATE TABLE security (
    ticker VARCHAR(255) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    strike DECIMAL(20,4),
    maturity DATE,
    underlying_ticker VARCHAR(255),
    mu DECIMAL(20,4),
    sigma DECIMAL(20,4),
    initial_price DECIMAL(20,4)
);