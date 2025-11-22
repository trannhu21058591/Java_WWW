package iuh.fit.se.shop_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestCartItemDTO implements Serializable {
    private Long productId;
    private String productName;
    private String productImageURL;
    private double price;
    private int quantity;
    private double subtotal;
}

