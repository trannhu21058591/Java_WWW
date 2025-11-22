package iuh.fit.se.shop_be.dto.response;

import iuh.fit.se.shop_be.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private List<ProductDTO> products;
    private long total; // Tổng số sản phẩm
}

