package iuh.fit.se.shop_be.services.Impl;

import iuh.fit.se.shop_be.entities.Category;
import iuh.fit.se.shop_be.exceptions.ResourceNotFoundException;
import iuh.fit.se.shop_be.repositories.CategoryRepository;
import iuh.fit.se.shop_be.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<Category> getAllCategories(boolean includeDeleted) {
        if (includeDeleted) {
            return categoryRepository.findAll();
        }
        return categoryRepository.findAllActive();
    }
    
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findByIdAndNotDeleted(id);
    }
    
    @Override
    @Transactional
    public Category createCategory(Category category) {
        category.setDeletedAt(null);
        return categoryRepository.save(category);
    }
    
    @Override
    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));
        
        // Cập nhật thông tin
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setImageURL(category.getImageURL());
        existingCategory.setActive(category.isActive());
        existingCategory.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(existingCategory);
    }
    
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // Soft delete - gỡ category (không xóa khỏi DB)
        Category category = categoryRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));
        
        category.setDeletedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
    
    @Override
    @Transactional
    public void restoreCategory(Long id) {
        // Khôi phục category đã xóa
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));
        
        if (category.getDeletedAt() == null) {
            throw new IllegalArgumentException("Danh mục này chưa bị xóa!");
        }
        
        category.setDeletedAt(null);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
    
    @Override
    @Transactional
    public void hardDeleteCategory(Long id) {
        // Xóa cứng - chỉ xóa khi chưa có sản phẩm nào
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));
        
        // Kiểm tra xem category có sản phẩm nào không
        if (categoryRepository.hasActiveProducts(id)) {
            throw new IllegalStateException("Không thể xóa danh mục này vì đã có sản phẩm!");
        }
        
        categoryRepository.delete(category);
    }
}

