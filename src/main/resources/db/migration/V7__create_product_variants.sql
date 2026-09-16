CREATE TABLE product_variants (
  id BIGINT AUTO_INCREMENT NOT NULL,
   sku VARCHAR(255) NOT NULL,
   product_id BIGINT NOT NULL,
   price DECIMAL(10, 2) NOT NULL,
   status VARCHAR(255) NULL,
   created_at datetime NULL,
   updated_at datetime NULL,
   CONSTRAINT pk_product_variants PRIMARY KEY (id)
);

ALTER TABLE product_variants ADD CONSTRAINT uc_product_variants_sku UNIQUE (sku);