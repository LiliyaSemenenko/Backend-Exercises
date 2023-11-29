-- A. product table
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL
);

-- B. product price table
CREATE TABLE product_price (
    product_id INT,
    price DECIMAL(10, 2) NOT NULL,
    discount_percent DECIMAL(5, 2) DEFAULT 0,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- C. product price change log table
CREATE TABLE product_price_change_log (
    product_id INT,
    old_price DECIMAL(10, 2),
    old_discount DECIMAL(5, 2),
    new_price DECIMAL(10, 2) NOT NULL,
    new_discount DECIMAL(5, 2) DEFAULT 0,
    change_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(255) NOT NULL,
    change_type ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- a query to join “product” table and “product price” table
SELECT
    product.name AS product_name,
    product.category AS product_category,
    product_price.price AS product_price,
    product_price.updated_by AS updated_by,
    product_price.updated_time AS updated_time
FROM product
JOIN product_price ON product.product_id = product_price.product_id;