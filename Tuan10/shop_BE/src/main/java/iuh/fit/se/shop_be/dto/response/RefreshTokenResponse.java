package iuh.fit.se.shop_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private String token; // New JWT token
}

