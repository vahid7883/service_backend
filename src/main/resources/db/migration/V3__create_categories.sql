CREATE TABLE categories (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   slug VARCHAR(255) NOT NULL,
   status VARCHAR(255) NULL,
   parent_id BIGINT NULL,
   CONSTRAINT pk_categories PRIMARY KEY (id)
);

ALTER TABLE categories ADD CONSTRAINT uc_categories_slug UNIQUE (slug);

ALTER TABLE categories ADD CONSTRAINT FK_CATEGORIES_ON_PARENT FOREIGN KEY (parent_id) REFERENCES categories (id);