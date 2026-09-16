CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT NOT NULL,
   order_id BIGINT NOT NULL,
   status VARCHAR(255) NULL,
   amount DECIMAL(10, 2) NOT NULL,
   payment_method_id BIGINT NULL,
   created_at datetime NOT NULL,
   updated_at datetime NOT NULL,
   CONSTRAINT pk_payments PRIMARY KEY (id)
);

ALTER TABLE payments ADD CONSTRAINT FK_PAYMENTS_ON_PAYMENT_METHOD FOREIGN KEY (payment_method_id) REFERENCES payment_methods (id);