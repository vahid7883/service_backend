package com.numjew.service_backend.checkout.service;


import com.numjew.service_backend.checkout.domain.CheckoutRequest;
import com.numjew.service_backend.checkout.domain.CheckoutResponse;

public interface CheckoutService {

    CheckoutResponse checkout(
            CheckoutRequest request,
            Long userId
    );


}