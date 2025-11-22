package iuh.fit.se.shop_be.repositories;

import iuh.fit.se.shop_be.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Tìm tất cả sản phẩm chưa bị xóa
    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL")
    List<Product> findAllActive();
    
    // Tìm sản phẩm theo ID và chưa bị xóa
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Product> findByIdAndNotDeleted(Long id);
    
    // Tìm sản phẩm theo category và chưa bị xóa
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.deletedAt IS NULL")
    List<Product> findByCategoryIdAndNotDeleted(Long categoryId);
    
    // Tìm sản phẩm active theo category (cho guest/customer)
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.active = true AND p.deletedAt IS NULL")
    List<Product> findActiveByCategoryId(Long categoryId);
    
    // Kiểm tra sản phẩm có trong đơn hàng nào không (để kiểm tra trước khi xóa cứng)
    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.product.id = :productId")
    boolean existsInOrder(Long productId);
    
    // Tìm tất cả sản phẩm đã bị xóa (để khôi phục)
    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NOT NULL")
    List<Product> findAllDeleted();
    
    // Tìm tất cả sản phẩm theo tên (chưa bị xóa)
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.deletedAt IS NULL")
    List<Product> findByNameContainingAndNotDeleted(String name);
    
    // Tìm tất cả sản phẩm active và chưa xóa (cho guest/customer)
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.deletedAt IS NULL")
    List<Product> findAllActiveAndNotDeleted();
    
    // Tìm sản phẩm active theo ID (cho guest/customer)
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.active = true AND p.deletedAt IS NULL")
    Optional<Product> findActiveById(Long id);
    
    // Tìm kiếm sản phẩm với nhiều điều kiện (cho guest/customer - chỉ lấy active)
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:gender IS NULL OR LOWER(p.gender) LIKE LOWER(CONCAT('%', :gender, '%'))) AND " +
           "(:material IS NULL OR LOWER(p.material) LIKE LOWER(CONCAT('%', :material, '%'))) AND " +
           "p.active = true AND p.deletedAt IS NULL")
    List<Product> searchActiveProducts(
            String name, 
            String brand, 
            Double minPrice, 
            Double maxPrice, 
            Long categoryId,
            String gender,
            String material);
}

