-- Users table
CREATE TABLE users (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role INT NOT NULL DEFAULT 1, -- 1 for user, 2 for admin
    PRIMARY KEY (user_id)
);
-- For SQL Server, the user_id column might be defined as:
-- user_id INT IDENTITY(1,1) PRIMARY KEY,
-- Or for PostgreSQL:
-- user_id SERIAL PRIMARY KEY,

-- Toys table
CREATE TABLE toys (
    toy_id INT NOT NULL AUTO_INCREMENT,
    toy_name VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    description TEXT,
    PRIMARY KEY (toy_id)
);
-- For SQL Server, the toy_id column might be defined as:
-- toy_id INT IDENTITY(1,1) PRIMARY KEY,
-- Or for PostgreSQL:
-- toy_id SERIAL PRIMARY KEY,

-- Default admin user insertion
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 2);
