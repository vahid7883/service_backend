package com.numjew.service_backend.shopping_basket.exception;

public class ShoppingBasketEmptyException extends  RuntimeException{
    public ShoppingBasketEmptyException() {
        super("basket is empty");
    }
}
