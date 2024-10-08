DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS warehouse CASCADE;
DROP TABLE IF EXISTS warehouse_item CASCADE;

CREATE TABLE IF NOT EXISTS item (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    volume INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS warehouse (
    id  SERIAL PRIMARY KEY,
    capacity INTEGER NOT NULL,
    location VARCHAR(255),
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS warehouse_item (
    id  SERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,
    item_id INTEGER,
    warehouse_id INTEGER
);