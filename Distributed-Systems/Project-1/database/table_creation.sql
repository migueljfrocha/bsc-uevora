BEGIN;

-- Users
CREATE TABLE users (
    user_id SERIAL NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    operational_state VARCHAR(30) NOT NULL,
    admin_state VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id)
);

-- Apartments
CREATE TABLE apartments (
    apartment_id SERIAL NOT NULL,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    type INT NOT NULL,
    operational_state VARCHAR(30) NOT NULL,
    admin_state VARCHAR(30) NOT NULL,
    owner_id INT NOT NULL,
    PRIMARY KEY (apartment_id)
);

-- Reservations
CREATE TABLE reservations (
    reservation_id SERIAL NOT NULL,
    apartment_id INT NOT NULL,
    renter_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    operational_state VARCHAR(30) NOT NULL,
    admin_state VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (reservation_id)
);

-- Foreign keys
ALTER TABLE reservations ADD FOREIGN KEY (apartment_id) REFERENCES apartments(apartment_id) ON DELETE RESTRICT;
ALTER TABLE reservations ADD FOREIGN KEY (renter_id) REFERENCES users(user_id) ON DELETE RESTRICT;
ALTER TABLE apartments ADD FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE RESTRICT;

COMMIT;