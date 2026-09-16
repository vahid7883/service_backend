package com.numjew.service_backend.shopping_basket.exception;

public class ShoppingBasketItemNotFoundException extends RuntimeException{
    public ShoppingBasketItemNotFoundException() {
        super("ShoppingBasketItem Not Found ");
    }
}
