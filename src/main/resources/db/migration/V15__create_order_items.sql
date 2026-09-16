CREATE TABLE order_items (
  id BIGINT AUTO_INCREMENT NOT NULL,
   order_id BIGINT NOT NULL,
   product_variant_id BIGINT NOT NULL,
   product_name VARCHAR(255) NOT NULL,
   sku VARCHAR(255) NOT NULL,
   unit_price DECIMAL(12, 2) NOT NULL,
   quantity INT NOT NULL,
   CONSTRAINT pk_order_items PRIMARY KEY (id)
);