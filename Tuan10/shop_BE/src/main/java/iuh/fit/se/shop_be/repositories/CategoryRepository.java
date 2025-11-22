package iuh.fit.se.shop_be.repositories;

import iuh.fit.se.shop_be.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Tìm tất cả category chưa bị xóa
    @Query("SELECT c FROM Category c WHERE c.deletedAt IS NULL")
    List<Category> findAllActive();
    
    // Tìm category theo ID và chưa bị xóa
    @Query("SELECT c FROM Category c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Category> findByIdAndNotDeleted(Long id);
    
    // Tìm category theo tên và chưa bị xóa
    Optional<Category> findByNameAndDeletedAtIsNull(String name);
    
    // Kiểm tra category có sản phẩm nào không (để kiểm tra trước khi xóa cứng)
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.category.id = :categoryId AND p.deletedAt IS NULL")
    boolean hasActiveProducts(Long categoryId);
    
    // Tìm tất cả category đã bị xóa (để khôi phục)
    @Query("SELECT c FROM Category c WHERE c.deletedAt IS NOT NULL")
    List<Category> findAllDeleted();
}

