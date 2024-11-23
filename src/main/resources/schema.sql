-- Create Publisher table
CREATE TABLE IF NOT EXISTS publisher (
    id INT PRIMARY KEY,
    publisher_name VARCHAR(255) NOT NULL
);
-- Insert some sample data into the Publisher table
INSERT INTO publisher (id, publisher_name) VALUES (1, 'Test Publisher');
INSERT INTO publisher (id, publisher_name) VALUES (2, 'Example Publisher');

