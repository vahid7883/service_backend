CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT NOT NULL,
   user_id BIGINT NOT NULL,
   status VARCHAR(255) NOT NULL,
   total_amount DECIMAL(12, 2) NOT NULL,
   created_at datetime NOT NULL,
   CONSTRAINT pk_orders PRIMARY KEY (id)
);