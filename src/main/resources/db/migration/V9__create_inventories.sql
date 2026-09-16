CREATE TABLE inventories (
  id BIGINT AUTO_INCREMENT NOT NULL,
   variant_id BIGINT NOT NULL,
   available_quantity INT NOT NULL,
   reserved_quantity INT NOT NULL,
   created_at datetime NULL,
   updated_at datetime NULL,
   CONSTRAINT pk_inventories PRIMARY KEY (id)
);

ALTER TABLE inventories ADD CONSTRAINT uc_85b10b8b5b3f52565751498e8 UNIQUE (variant_id);