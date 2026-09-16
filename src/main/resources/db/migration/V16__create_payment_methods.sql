CREATE TABLE payment_methods (
  id BIGINT AUTO_INCREMENT NOT NULL,
   user_id BIGINT NOT NULL,
   payment_type VARCHAR(255) NULL,
   token VARCHAR(255) NOT NULL,
   metadata VARCHAR(2000) NULL,
   CONSTRAINT pk_payment_methods PRIMARY KEY (id)
);