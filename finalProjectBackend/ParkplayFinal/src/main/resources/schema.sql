CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    license_plate VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS parking_lots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    manager_id BIGINT,
    FOREIGN KEY (manager_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS parking_spots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    spot_number VARCHAR(50) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    hourly_rate DOUBLE NOT NULL,
    parking_lot_id BIGINT,
    FOREIGN KEY (parking_lot_id) REFERENCES parking_lots(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    spot_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    FOREIGN KEY (driver_id) REFERENCES users(id),
    FOREIGN KEY (spot_id) REFERENCES parking_spots(id)
);