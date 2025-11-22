package iuh.fit.se.shop_fe.modelDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private long id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private int stock;
    private String imageURL;
    private String categoryName;
    private String gender;
}

