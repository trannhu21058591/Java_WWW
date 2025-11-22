package iuh.fit.se.shop_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
    
    private String description;
    private String imageURL;        // Ảnh danh mục (base64)
    
    private Boolean active = true;   // Trạng thái hoạt động (mặc định là true)
}

