package com.numjew.service_backend.checkout.service;

import com.numjew.service_backend.auth.AuthService;
import com.numjew.service_backend.checkout.domain.CheckoutRequest;
import com.numjew.service_backend.checkout.domain.CheckoutResponse;
import com.numjew.service_backend.order.repository.OrderRepository;
import com.numjew.service_backend.shopping_basket.repository.ShoppingBasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    // dependencies
    private final ShoppingBasketRepository shoppingBasketRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @Override
    public CheckoutResponse checkout(CheckoutRequest request, Long userId) {
//        var user = authService.getCurrentUser();
//        var userId = user.getId();
//        var basket = shoppingBasketRepository.findById()
//        var order = orderRepository.findByUserId(userId);
//        var orderId = order.getId();
//        var result = shoppingBasketRepository.findByIdAndUserId(request.getShoppingBasketId(), userId);
//        return new CheckoutResponse(orderId,"have not implement gateway yet");
//        var basket = shoppingBasketRepository.
        return null;
    }



//    @Transactional
//    public CheckoutResponse checkout(CheckoutRequest request) {
//        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
//        if (cart == null) {
//            throw new CartNotFoundException();
//        }
//
//        if (cart.isEmpty()) {
//            throw new CartEmptyException();
//        }
//
//        var order = Order.fromCart(cart, authService.getCurrentUser());
//
//        orderRepository.save(order);
//
//        try {
//            var session = paymentGateway.createCheckoutSession(order);
//
//            cartService.clearCart(cart.getId());
//
//            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
//        }
//        catch (PaymentException ex) {
//            orderRepository.delete(order);
//            throw ex;
//        }
//    }

}
