package iuh.fit.se.shop_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProductDTO {
    private long id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private int stock;
    
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String imageURL; 
    
    private String categoryName;
    private String gender; 
}

