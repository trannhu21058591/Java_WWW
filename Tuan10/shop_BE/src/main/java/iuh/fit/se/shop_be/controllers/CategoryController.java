package iuh.fit.se.shop_be.controllers;

import iuh.fit.se.shop_be.Enum.ResponseCode;
import iuh.fit.se.shop_be.dto.CategoryDTO;
import iuh.fit.se.shop_be.dto.request.CreateCategoryRequest;
import iuh.fit.se.shop_be.dto.request.UpdateCategoryRequest;
import iuh.fit.se.shop_be.dto.response.CategoryDetailResponse;
import iuh.fit.se.shop_be.dto.response.CategoryListResponse;
import iuh.fit.se.shop_be.entities.Category;
import iuh.fit.se.shop_be.exceptions.ResourceNotFoundException;
import iuh.fit.se.shop_be.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * GET /api/categories
     * Lấy danh sách tất cả danh mục đang hoạt động (không bao gồm đã xóa)
     * Không cần token
     */
    @GetMapping
    public ResponseEntity<CategoryListResponse> getAllCategories(
            @RequestParam(required = false, defaultValue = "false") boolean includeDeleted) {
        try {
            List<Category> categories = categoryService.getAllCategories(includeDeleted);
            
            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            CategoryListResponse response = CategoryListResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Lấy danh sách danh mục thành công!")
                    .success(true)
                    .categories(categoryDTOs)
                    .total(categoryDTOs.size())
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách danh mục: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CategoryListResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi lấy danh sách danh mục: " + e.getMessage())
                            .success(false)
                            .categories(List.of())
                            .total(0)
                            .build());
        }
    }

    /**
     * POST /api/categories/admin/create
     * Tạo mới danh mục (chỉ dành cho ADMIN)
     * Cần token với role ADMIN
     */
    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDetailResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        try {
            Category category = Category.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .imageURL(request.getImageURL())
                    .active(request.getActive() != null ? request.getActive() : true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(null)
                    .build();

            Category savedCategory = categoryService.createCategory(category);

            CategoryDTO categoryDTO = convertToDTO(savedCategory);

            CategoryDetailResponse response = CategoryDetailResponse.builder()
                    .returnCode(ResponseCode.CREATED.getCode())
                    .message("Tạo danh mục thành công!")
                    .success(true)
                    .category(categoryDTO)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Lỗi khi tạo danh mục: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CategoryDetailResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi tạo danh mục: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * PUT /api/categories/admin/update
     * Cập nhật danh mục (chỉ dành cho ADMIN)
     * Cần token với role ADMIN
     */
    @PutMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDetailResponse> updateCategory(
            @Valid @RequestBody UpdateCategoryRequest request) {
        try {
            Category existingCategory = categoryService.getCategoryById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy danh mục với ID: " + request.getId()));

            Category categoryToUpdate = Category.builder()
                    .name(request.getName() != null ? request.getName() : existingCategory.getName())
                    .description(request.getDescription() != null ? request.getDescription() : existingCategory.getDescription())
                    .imageURL(request.getImageURL() != null ? request.getImageURL() : existingCategory.getImageURL())
                    .active(request.getActive() != null ? request.getActive() : existingCategory.isActive())
                    .build();

            Category updatedCategory = categoryService.updateCategory(request.getId(), categoryToUpdate);

            CategoryDTO categoryDTO = convertToDTO(updatedCategory);

            CategoryDetailResponse response = CategoryDetailResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Cập nhật danh mục thành công!")
                    .success(true)
                    .category(categoryDTO)
                    .build();

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.error("Lỗi khi cập nhật danh mục: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CategoryDetailResponse.builder()
                            .returnCode(ResponseCode.NOT_FOUND.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật danh mục: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CategoryDetailResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi cập nhật danh mục: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * GET /api/categories/{id}
     * Lấy chi tiết danh mục theo ID
     * Không cần token
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));
            
            CategoryDTO categoryDTO = convertToDTO(category);
            
            return ResponseEntity.ok(categoryDTO);
        } catch (ResourceNotFoundException e) {
            log.error("Không tìm thấy danh mục: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Lỗi khi lấy chi tiết danh mục: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Convert Category entity to CategoryDTO
     */
    private CategoryDTO convertToDTO(Category category) {
        String imageURL = category.getImageURL();

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageURL(imageURL)
                .active(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}

