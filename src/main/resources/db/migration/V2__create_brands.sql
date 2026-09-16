CREATE TABLE brands (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL,
  slug VARCHAR(255) NOT NULL,
  logo_url VARCHAR(255) NOT NULL,
  `description` VARCHAR(1000) NULL,
  created_at datetime NULL,
  updated_at datetime NULL,
  CONSTRAINT pk_brands PRIMARY KEY (id)
);

ALTER TABLE brands ADD CONSTRAINT uc_brands_name UNIQUE (name);

ALTER TABLE brands ADD CONSTRAINT uc_brands_slug UNIQUE (slug);