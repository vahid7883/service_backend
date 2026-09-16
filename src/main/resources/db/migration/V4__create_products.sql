CREATE TABLE products (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   `description` VARCHAR(1000) NULL,
   status VARCHAR(255) NULL,
   brand_id BIGINT NOT NULL,
   slug VARCHAR(255) NOT NULL,
   created_at datetime NULL,
   updated_at datetime NULL,
   CONSTRAINT pk_products PRIMARY KEY (id)
);

ALTER TABLE products ADD CONSTRAINT uc_products_slug UNIQUE (slug);