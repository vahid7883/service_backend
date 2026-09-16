CREATE TABLE stock_reservations (
  id BIGINT AUTO_INCREMENT NOT NULL,
   variant_id BIGINT NOT NULL,
   quantity INT NOT NULL,
   reservation_reference VARCHAR(255) NOT NULL,
   status VARCHAR(255) NULL,
   expires_at datetime NOT NULL,
   created_at datetime NULL,
   CONSTRAINT pk_stock_reservations PRIMARY KEY (id)
);