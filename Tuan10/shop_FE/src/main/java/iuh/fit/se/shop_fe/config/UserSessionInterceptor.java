package iuh.fit.se.shop_fe.config;

import iuh.fit.se.shop_fe.modelDTOs.CategoryListResponse;
import iuh.fit.se.shop_fe.modelDTOs.UserDTO;
import iuh.fit.se.shop_fe.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class UserSessionInterceptor implements HandlerInterceptor {

    private final CategoryService categoryService;

    @Override
    public void postHandle(jakarta.servlet.http.HttpServletRequest request,
                          jakarta.servlet.http.HttpServletResponse response,
                          Object handler,
                          ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                UserDTO user = (UserDTO) session.getAttribute("user");
                // Tự động thêm user vào model nếu chưa có
                if (user != null && modelAndView.getModel().get("user") == null) {
                    modelAndView.addObject("user", user);
                }
            }
            
            // Tự động thêm categories vào model nếu chưa có
            if (modelAndView.getModel().get("categories") == null) {
                try {
                    CategoryListResponse categoryResponse = categoryService.getAllCategories();
                    if (categoryResponse != null && categoryResponse.isSuccess() && categoryResponse.getCategories() != null) {
                        modelAndView.addObject("categories", categoryResponse.getCategories());
                    }
                } catch (Exception e) {
                    // Nếu lỗi, không thêm categories vào model
                }
            }
        }
    }
}

