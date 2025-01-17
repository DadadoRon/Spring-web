CREATE TABLE IF NOT EXISTS users (
 id INTEGER PRIMARY KEY,
 first_name VARCHAR(200) NOT NULL,
 last_name VARCHAR(255) NOT NULL,
 email VARCHAR(254) NOT NULL,
 password VARCHAR(255) NOT NULL,
 role VARCHAR(50) NOT NULL
);
CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 3 INCREMENT BY 1;

-- CREATE SEQUENCE IF NOT EXISTS products_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price NUMERIC(10, 2) NOT NULL,
  image_name VARCHAR(255)
);
ALTER TABLE products
    ADD CONSTRAINT name_unique_constraint UNIQUE (name);




