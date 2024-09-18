-- Tworzenie tabeli użytkowników
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100),
                       enabled BOOLEAN DEFAULT TRUE
);

-- Tworzenie tabeli pojazdów
CREATE TABLE vehicles (
                          id BIGSERIAL PRIMARY KEY,
                          make VARCHAR(50),
                          model VARCHAR(50),
                          year INTEGER,
                          registration_number VARCHAR(20),
                          user_id BIGINT NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tworzenie tabeli tankowań
CREATE TABLE fuel_entries (
                              id BIGSERIAL PRIMARY KEY,
                              date DATE NOT NULL,
                              mileage INTEGER,
                              fuel_volume NUMERIC(10,2),
                              price_per_unit NUMERIC(10,2),
                              discount NUMERIC(10,2),
                              fuel_type VARCHAR(20),
                              gas_station VARCHAR(100),
                              notes TEXT,
                              vehicle_id BIGINT NOT NULL,
                              FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);
