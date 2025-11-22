package iuh.fit.se.shop_fe.controllers;

import iuh.fit.se.shop_fe.modelDTOs.ProductDetailDTO;
import iuh.fit.se.shop_fe.modelDTOs.ProductDetailResponse;
import iuh.fit.se.shop_fe.modelDTOs.ProductDTO;
import iuh.fit.se.shop_fe.modelDTOs.ProductListResponse;
import iuh.fit.se.shop_fe.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Value("${backend.api.url}")
    private String backendApiUrl;


    private String normalizeImageURL(String imageURL) {
        if (imageURL == null || imageURL.isEmpty()) {
            return imageURL;
        }

        // Nếu là base64 hoặc full URL (http/https), giữ nguyên
        if (imageURL.startsWith("data:image/") || imageURL.startsWith("http://") || imageURL.startsWith("https://")) {
            return imageURL;
        }
        
        // Chỉ normalize nếu là đường dẫn static images hợp lệ (bắt đầu bằng /images/)
        if (imageURL.startsWith("/images/")) {
            return backendApiUrl + imageURL;
        }
        
        // Nếu là đường dẫn tương đối khác (như /products/xxx.jpg), không normalize
        // Vì chỉ dùng base64 từ DB, các URL này sẽ không được serve
        if (imageURL.startsWith("/")) {
            return null; // Hoặc return imageURL để giữ nguyên nhưng sẽ không load được
        }
        
        // Nếu không phải base64 và không phải URL hợp lệ, return null
        return null;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) Long category) {
        try {
            ProductListResponse response;
            if (category != null) {
                response = productService.getProductsByCategory(category);
            } else {
                response = productService.getAllProducts();
            }
            
            if (response != null && response.isSuccess() && response.getProducts() != null) {

                List<ProductDTO> products = response.getProducts().stream()
                        .map(product -> {
                            String normalizedImageURL = normalizeImageURL(product.getImageURL());
                            product.setImageURL(normalizedImageURL);
                            return product;
                        })
                        .collect(Collectors.toList());
                
                model.addAttribute("products", products);
                model.addAttribute("total", response.getTotal());
                model.addAttribute("message", response.getMessage());
                if (category != null) {
                    model.addAttribute("selectedCategory", category);
                }
            } else {
                model.addAttribute("products", java.util.Collections.emptyList());
                model.addAttribute("total", 0);
                model.addAttribute("message", "Không có sản phẩm nào");
            }
        } catch (Exception e) {
            model.addAttribute("products", java.util.Collections.emptyList());
            model.addAttribute("total", 0);
            model.addAttribute("error", "Lỗi khi tải danh sách sản phẩm: " + e.getMessage());
        }
        
        return "views/Home";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        try {
            ProductDetailResponse response = productService.getProductById(id);
            if (response != null && response.isSuccess() && response.getProduct() != null) {
                ProductDetailDTO product = response.getProduct();
                String normalizedImageURL = normalizeImageURL(product.getImageURL());
                product.setImageURL(normalizedImageURL);
                
                model.addAttribute("product", product);
            } else {
                model.addAttribute("error", "Không tìm thấy sản phẩm");
                return "views/Home";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải chi tiết sản phẩm: " + e.getMessage());
            return "views/Home";
        }
        
        return "views/ProductDetail";
    }
}



