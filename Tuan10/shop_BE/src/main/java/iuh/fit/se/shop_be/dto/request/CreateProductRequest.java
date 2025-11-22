package iuh.fit.se.shop_be.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    
    private String brand;
    private String modelNumber;
    
    private String description;
    
    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    private Double price;
    
    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho phải lớn hơn hoặc bằng 0")
    private Integer stock;
    
    private String material;        // Chất liệu (Thép, vàng, bạc, da, ...)
    private String movement;        // Bộ máy (Quartz, Automatic, Mechanical, ...)
    private String gender;          // Giới tính (Nam, Nữ, Unisex)
    private String diaColor;        // Màu mặt đồng hồ
    private String strapColor;      // Màu dây đeo
    private String caseSize;        // Kích thước vỏ (mm)
    private String waterResistance; // Độ chống nước
    
    private String imageURL;        // Ảnh chính (base64)
    private String imageURLs;       // Ảnh phụ (base64, phân cách bởi dấu phẩy)
    
    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;        // ID của category
    
    private Boolean active = true;   // Trạng thái hoạt động (mặc định là true)
}

