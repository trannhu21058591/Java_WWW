package iuh.fit.se.shop_fe.services.impl;

import iuh.fit.se.shop_fe.modelDTOs.AddToCartRequest;
import iuh.fit.se.shop_fe.modelDTOs.AddToCartResponse;
import iuh.fit.se.shop_fe.modelDTOs.CartListResponse;
import iuh.fit.se.shop_fe.services.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    @Value("${backend.api.url}")
    private String backendApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public AddToCartResponse addToCart(Long productId, Integer quantity) {
        return addToCart(productId, quantity, null);
    }

    public AddToCartResponse addToCart(Long productId, Integer quantity, HttpServletRequest request) {
        try {
            String url = backendApiUrl + "/api/cart/add";
            
            AddToCartRequest requestBody = AddToCartRequest.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Forward cookie từ request nếu có
            if (request != null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    StringBuilder cookieHeader = new StringBuilder();
                    for (Cookie cookie : cookies) {
                        if (cookieHeader.length() > 0) {
                            cookieHeader.append("; ");
                        }
                        cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
                    }
                    if (cookieHeader.length() > 0) {
                        headers.add("Cookie", cookieHeader.toString());
                    }
                }
            }
            
            HttpEntity<AddToCartRequest> httpEntity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<AddToCartResponse> response = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    AddToCartResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi thêm vào giỏ hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public CartListResponse getCartItems() {
        return getCartItems(null);
    }

    public CartListResponse getCartItems(HttpServletRequest request) {
        try {
            String url = backendApiUrl + "/api/cart";
            
            HttpHeaders headers = new HttpHeaders();
            
            // Forward cookie từ request nếu có
            if (request != null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    StringBuilder cookieHeader = new StringBuilder();
                    for (Cookie cookie : cookies) {
                        if (cookieHeader.length() > 0) {
                            cookieHeader.append("; ");
                        }
                        cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
                    }
                    if (cookieHeader.length() > 0) {
                        headers.add("Cookie", cookieHeader.toString());
                    }
                }
            }
            
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
            
            ResponseEntity<CartListResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    CartListResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Lỗi khi lấy giỏ hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi lấy giỏ hàng: " + e.getMessage(), e);
        }
    }
}

