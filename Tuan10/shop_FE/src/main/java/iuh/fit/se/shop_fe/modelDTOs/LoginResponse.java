package iuh.fit.se.shop_fe.modelDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private UserDTO user;
    private String token;
}

