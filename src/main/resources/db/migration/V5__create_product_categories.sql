CREATE TABLE product_categories (
  id BIGINT AUTO_INCREMENT NOT NULL,
   product_id BIGINT NOT NULL,
   category_id BIGINT NOT NULL,
   CONSTRAINT pk_product_categories PRIMARY KEY (id)
);

ALTER TABLE product_categories ADD CONSTRAINT uc_3d0d0e623423b7bf1c17a4d5d UNIQUE (product_id, category_id);