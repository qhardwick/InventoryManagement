
BEGIN;

-- Create the Main Warehouse:
INSERT INTO warehouse (name, location, capacity) VALUES ('Main Warehouse', 'Dallas, TX', 10000);

-- Define some items:
INSERT INTO item (name, volume) VALUES ('Car', 500);
COMMIT;