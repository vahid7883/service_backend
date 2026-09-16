CREATE TABLE shopping_basket (
  id BINARY(16) NOT NULL,
   user_id BIGINT NOT NULL,
   created_at datetime NOT NULL,
   CONSTRAINT pk_shopping_basket PRIMARY KEY (id)
);