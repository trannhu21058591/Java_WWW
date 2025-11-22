package iuh.fit.se.shop_fe.controllers;

import iuh.fit.se.shop_fe.modelDTOs.CartListResponse;
import iuh.fit.se.shop_fe.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    /**
     * GET /cart
     * Hiển thị trang giỏ hàng
     */
    @GetMapping("/cart")
    public String cartPage(Model model, HttpServletRequest request) {
        try {
            CartListResponse response = cartService.getCartItems(request);
            
            if (response != null && response.isSuccess()) {
                model.addAttribute("cartItems", response.getItems());
                model.addAttribute("totalPrice", response.getTotalPrice());
                model.addAttribute("totalItems", response.getTotalItems());
            } else {
                model.addAttribute("cartItems", java.util.Collections.emptyList());
                model.addAttribute("totalPrice", 0.0);
                model.addAttribute("totalItems", 0);
            }
        } catch (Exception e) {
            log.error("Lỗi khi lấy giỏ hàng: {}", e.getMessage(), e);
            model.addAttribute("error", "Lỗi khi tải giỏ hàng: " + e.getMessage());
            model.addAttribute("cartItems", java.util.Collections.emptyList());
            model.addAttribute("totalPrice", 0.0);
            model.addAttribute("totalItems", 0);
        }
        
        return "views/Cart";
    }

    /**
     * POST /cart/add
     * Thêm sản phẩm vào giỏ hàng
     */
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam(required = false) String redirectUrl,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(productId, quantity, request);
            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
            
            // Redirect về trang trước đó hoặc trang chi tiết sản phẩm
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/cart";
        } catch (Exception e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
            
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/";
        }
    }
}

