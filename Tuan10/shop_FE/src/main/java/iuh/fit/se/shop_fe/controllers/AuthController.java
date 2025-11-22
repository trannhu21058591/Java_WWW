package iuh.fit.se.shop_fe.controllers;

import iuh.fit.se.shop_fe.modelDTOs.LoginResponse;
import iuh.fit.se.shop_fe.modelDTOs.UserDTO;
import iuh.fit.se.shop_fe.services.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "views/auth/Login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            LoginResponse response = authService.login(email, password);
            
            if (response != null && response.isSuccess() && response.getUser() != null) {
                // Lưu thông tin user vào session
                session.setAttribute("user", response.getUser());
                session.setAttribute("token", response.getToken());
                
                // Đảm bảo session không bị invalidate
                session.setMaxInactiveInterval(30 * 60); // 30 phút
                
                log.info("Login - Session ID: {}, User saved: {}, Session max inactive: {} seconds", 
                        session.getId(), 
                        response.getUser().getEmail(),
                        session.getMaxInactiveInterval());
                
                redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại!");
                return "redirect:/login";
            }
        } catch (Exception e) {
            log.error("Lỗi khi đăng nhập: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "views/auth/Register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            LoginResponse response = authService.register(email, password, fullName, phone, address);
            
            if (response != null && response.isSuccess() && response.getUser() != null) {
                // Lưu thông tin user vào session
                session.setAttribute("user", response.getUser());
                session.setAttribute("token", response.getToken());
                
                redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại!");
                return "redirect:/register";
            }
        } catch (Exception e) {
            log.error("Lỗi khi đăng ký: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

