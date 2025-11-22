package iuh.fit.se.shop_be.services;

import iuh.fit.se.shop_be.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts(boolean includeDeleted);
    List<Product> getActiveProducts(); // Chỉ lấy sản phẩm active và chưa xóa
    Optional<Product> getProductById(Long id);
    Optional<Product> getActiveProductById(Long id); // Lấy sản phẩm active theo ID
    List<Product> getProductsByCategory(Long categoryId);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id); // gỡ sản phẩm
    void restoreProduct(Long id); // Khôi phục sản phẩm đã xóa
    void hardDeleteProduct(Long id); // Xóa cứng (chỉ khi chưa có trong đơn hàng)
    List<Product> searchProducts(String keyword);
    List<Product> searchActiveProducts(String name, String brand, Double minPrice, Double maxPrice, Long categoryId, String gender, String material);
}

