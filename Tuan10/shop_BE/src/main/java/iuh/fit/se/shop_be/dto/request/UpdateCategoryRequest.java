package iuh.fit.se.shop_be.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {
    @NotNull(message = "ID danh mục không được để trống")
    private Long id;
    
    private String name;
    private String description;
    private String imageURL;        // Ảnh danh mục (base64)
    private Boolean active;         // Trạng thái hoạt động
}

