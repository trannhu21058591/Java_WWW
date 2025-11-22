package iuh.fit.se.shop_fe.services;

import iuh.fit.se.shop_fe.modelDTOs.AddToCartResponse;
import iuh.fit.se.shop_fe.modelDTOs.CartListResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
    AddToCartResponse addToCart(Long productId, Integer quantity);
    AddToCartResponse addToCart(Long productId, Integer quantity, HttpServletRequest request);
    CartListResponse getCartItems();
    CartListResponse getCartItems(HttpServletRequest request);
}

