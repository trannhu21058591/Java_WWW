package iuh.fit.se.shop_be.controllers;

import iuh.fit.se.shop_be.dto.GuestCartItemDTO;
import iuh.fit.se.shop_be.dto.request.AddToCartRequest;
import iuh.fit.se.shop_be.dto.response.AddToCartResponse;
import iuh.fit.se.shop_be.dto.response.CartListResponse;
import iuh.fit.se.shop_be.Enum.ResponseCode;
import iuh.fit.se.shop_be.entities.User;
import iuh.fit.se.shop_be.repositories.UserRepository;
import iuh.fit.se.shop_be.services.CartService;
import iuh.fit.se.shop_be.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;
    private static final String GUEST_CART_SESSION_KEY = "guestCart";

    /**
     * POST /api/cart/add
     * Thêm sản phẩm vào giỏ hàng
     * - Nếu đã đăng nhập: lưu vào database
     * - Nếu là guest: lưu vào session
     */
    @PostMapping("/add")
    public ResponseEntity<AddToCartResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            HttpServletRequest httpRequest,
            HttpSession session) {
        try {
            // Kiểm tra user đã đăng nhập chưa
            Long userId = (Long) httpRequest.getAttribute("userId");
            
            if (userId == null) {
                String email = SecurityUtil.getCurrentUserEmail();
                if (email != null && !email.equals("anonymousUser")) {
                    User user = userRepository.findByEmail(email).orElse(null);
                    if (user != null) {
                        userId = user.getId();
                    }
                }
            }

            AddToCartResponse response;
            
            if (userId != null) {
                // User đã đăng nhập - lưu vào database
                response = cartService.addToCart(userId, request);
            } else {
                // Guest - lưu vào session
                response = cartService.addToGuestCart(request);
                
                // Lưu vào session
                @SuppressWarnings("unchecked")
                List<GuestCartItemDTO> guestCart = (List<GuestCartItemDTO>) session.getAttribute(GUEST_CART_SESSION_KEY);
                if (guestCart == null) {
                    guestCart = new ArrayList<>();
                }
                
                // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
                boolean found = false;
                for (GuestCartItemDTO item : guestCart) {
                    if (item.getProductId().equals(request.getProductId())) {
                        // Cộng dồn quantity
                        item.setQuantity(item.getQuantity() + request.getQuantity());
                        item.setSubtotal(item.getPrice() * item.getQuantity());
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    // Thêm mới
                    GuestCartItemDTO newItem = GuestCartItemDTO.builder()
                            .productId(response.getCartItem().getProductId())
                            .productName(response.getCartItem().getProductName())
                            .productImageURL(response.getCartItem().getProductImageURL())
                            .price(response.getCartItem().getPrice())
                            .quantity(response.getCartItem().getQuantity())
                            .subtotal(response.getCartItem().getSubtotal())
                            .build();
                    guestCart.add(newItem);
                }
                
                session.setAttribute(GUEST_CART_SESSION_KEY, guestCart);
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("Lỗi khi thêm sản phẩm vào giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AddToCartResponse.builder()
                            .returnCode(ResponseCode.BAD_REQUEST.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi thêm sản phẩm vào giỏ hàng: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AddToCartResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi thêm sản phẩm vào giỏ hàng: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * GET /api/cart
     * Lấy danh sách sản phẩm trong giỏ hàng
     * - Nếu đã đăng nhập: lấy từ database
     * - Nếu là guest: lấy từ session
     */
    @GetMapping
    public ResponseEntity<CartListResponse> getCartItems(
            HttpServletRequest httpRequest,
            HttpSession session) {
        try {
            // Kiểm tra user đã đăng nhập chưa
            Long userId = (Long) httpRequest.getAttribute("userId");
            
            if (userId == null) {
                String email = SecurityUtil.getCurrentUserEmail();
                if (email != null && !email.equals("anonymousUser")) {
                    User user = userRepository.findByEmail(email).orElse(null);
                    if (user != null) {
                        userId = user.getId();
                    }
                }
            }

            CartListResponse response;
            
            if (userId != null) {
                // User đã đăng nhập - lấy từ database
                response = cartService.getCartItems(userId);
            } else {
                // Guest - lấy từ session
                @SuppressWarnings("unchecked")
                List<GuestCartItemDTO> guestCart = (List<GuestCartItemDTO>) session.getAttribute(GUEST_CART_SESSION_KEY);
                if (guestCart == null) {
                    guestCart = new ArrayList<>();
                }
                response = cartService.getGuestCartItems(guestCart);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách giỏ hàng: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CartListResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi lấy danh sách giỏ hàng: " + e.getMessage())
                            .success(false)
                            .items(new ArrayList<>())
                            .totalPrice(0.0)
                            .totalItems(0)
                            .build());
        }
    }

    /**
     * POST /api/cart/merge
     * Merge giỏ hàng guest vào giỏ hàng user khi đăng nhập
     * Chỉ dùng nội bộ, không cần expose endpoint này
     */
    public void mergeGuestCart(Long userId, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<GuestCartItemDTO> guestCart = (List<GuestCartItemDTO>) session.getAttribute(GUEST_CART_SESSION_KEY);
        if (guestCart != null && !guestCart.isEmpty()) {
            cartService.mergeGuestCartToUserCart(userId, guestCart);
            // Xóa giỏ hàng guest sau khi merge
            session.removeAttribute(GUEST_CART_SESSION_KEY);
        }
    }
}

