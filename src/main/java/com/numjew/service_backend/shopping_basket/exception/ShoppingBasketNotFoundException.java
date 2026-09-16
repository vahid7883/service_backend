package com.numjew.service_backend.shopping_basket.exception;

public class ShoppingBasketNotFoundException extends RuntimeException{
    public ShoppingBasketNotFoundException(){
        super("shopping-basket not found");
    }
}
