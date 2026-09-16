CREATE TABLE product_images (
  id BIGINT AUTO_INCREMENT NOT NULL,
   product_id BIGINT NOT NULL,
   image_url VARCHAR(255) NOT NULL,
   alt_text VARCHAR(255) NULL,
   primary_image BIT(1) NULL,
   display_order INT NULL,
   created_at datetime NULL,
   CONSTRAINT pk_product_images PRIMARY KEY (id)
);