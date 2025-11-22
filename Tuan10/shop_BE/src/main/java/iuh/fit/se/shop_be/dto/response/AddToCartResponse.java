package iuh.fit.se.shop_be.dto.response;

import iuh.fit.se.shop_be.Enum.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private CartItemDTO cartItem;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDTO {
        private Long id;
        private Long productId;
        private String productName;
        private String productImageURL;
        private double price;
        private int quantity;
        private double subtotal;
    }
}

