package iuh.fit.se.shop_be.dto.response;

import iuh.fit.se.shop_be.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private UserDTO user;
    private String token; // JWT token
}

