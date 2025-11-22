package iuh.fit.se.shop_be.services;

import iuh.fit.se.shop_be.dto.GuestCartItemDTO;
import iuh.fit.se.shop_be.dto.request.AddToCartRequest;
import iuh.fit.se.shop_be.dto.response.AddToCartResponse;
import iuh.fit.se.shop_be.dto.response.CartListResponse;

import java.util.List;

public interface CartService {
    AddToCartResponse addToCart(Long userId, AddToCartRequest request);
    AddToCartResponse addToGuestCart(AddToCartRequest request);
    void mergeGuestCartToUserCart(Long userId, List<GuestCartItemDTO> guestCartItems);
    CartListResponse getCartItems(Long userId);
    CartListResponse getGuestCartItems(List<GuestCartItemDTO> guestCartItems);
}

