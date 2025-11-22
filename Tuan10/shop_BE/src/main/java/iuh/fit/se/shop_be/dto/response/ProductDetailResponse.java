package iuh.fit.se.shop_be.dto.response;

import iuh.fit.se.shop_be.dto.ProductDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private ProductDetailDTO product;
}

