package iuh.fit.se.shop_be.services;

import iuh.fit.se.shop_be.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories(boolean includeDeleted);
    Optional<Category> getCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long id); // gỡ category
    void restoreCategory(Long id); // Khôi phục category đã xóa
    void hardDeleteCategory(Long id); // Xóa cứng (chỉ khi chưa có sản phẩm)
}

