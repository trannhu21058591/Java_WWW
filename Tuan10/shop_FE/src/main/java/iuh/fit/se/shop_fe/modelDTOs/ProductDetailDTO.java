package iuh.fit.se.shop_fe.modelDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private long id;
    private String name;
    private String brand;
    private String modelNumber;
    private String description;
    private double price;
    private int stock;

    private String material;
    private String movement;
    private String gender;
    private String diaColor;
    private String strapColor;
    private String caseSize;
    private String waterResistance;
    
    private String imageURL;
    private int sold;
    private boolean active;
    
    // Category info
    private Long categoryId;
    private String categoryName;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

