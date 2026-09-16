CREATE TABLE shopping_basket_items (
  id BIGINT AUTO_INCREMENT NOT NULL,
   basket_id BINARY(16) NOT NULL,
   product_variant_id BIGINT NOT NULL,
   quantity INT NOT NULL,
   CONSTRAINT pk_shopping_basket_items PRIMARY KEY (id)
);

ALTER TABLE shopping_basket_items ADD CONSTRAINT uk_basket_variant UNIQUE (basket_id, product_variant_id);

ALTER TABLE shopping_basket_items ADD CONSTRAINT FK_SHOPPING_BASKET_ITEMS_ON_BASKET FOREIGN KEY (basket_id) REFERENCES shopping_basket (id);