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
public class UserDTO {
    private long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String role; 
    private LocalDateTime createdAt;
}

