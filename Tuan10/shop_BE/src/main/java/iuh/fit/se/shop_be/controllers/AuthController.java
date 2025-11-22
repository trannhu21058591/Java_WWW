package iuh.fit.se.shop_be.controllers;

import iuh.fit.se.shop_be.Enum.ResponseCode;
import iuh.fit.se.shop_be.Enum.Role;
import iuh.fit.se.shop_be.dto.UserDTO;
import iuh.fit.se.shop_be.dto.request.AdminCreateUserRequest;
import iuh.fit.se.shop_be.dto.request.LoginRequest;
import iuh.fit.se.shop_be.dto.request.RegisterRequest;
import iuh.fit.se.shop_be.dto.response.LoginResponse;
import iuh.fit.se.shop_be.dto.response.RefreshTokenResponse;
import iuh.fit.se.shop_be.dto.response.RegisterResponse;
import iuh.fit.se.shop_be.entities.User;
import iuh.fit.se.shop_be.services.AuthService;
import iuh.fit.se.shop_be.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());

        String token = authService.generateToken(user);


        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();


        LoginResponse response = LoginResponse.builder()
                .returnCode(ResponseCode.SUCCESS.getCode())
                .message("Đăng nhập thành công!")
                .success(true)
                .user(userDTO)
                .token(token)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(
                request.getEmail(),
                request.getPassword(),
                request.getFullName(),
                request.getPhone(),
                request.getAddress()
        );

  
        String token = authService.generateToken(user);

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();

        RegisterResponse response = RegisterResponse.builder()
                .returnCode(ResponseCode.CREATED.getCode())
                .message("Đăng ký thành công!")
                .success(true)
                .user(userDTO)
                .token(token)
                .build();

        return ResponseEntity.status(ResponseCode.CREATED.getCode()).body(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token không được để trống!");
        }

        String newToken = authService.refreshToken(refreshToken);

        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .returnCode(ResponseCode.SUCCESS.getCode())
                .message("Làm mới token thành công!")
                .success(true)
                .token(newToken)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint cho Admin tạo user với role bất kỳ (kể cả ADMIN)
     * Chỉ admin mới có quyền truy cập endpoint này
     */
    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponse> createUserByAdmin(@Valid @RequestBody AdminCreateUserRequest request) {
        // @PreAuthorize và SecurityConfig đã kiểm tra quyền ADMIN

        User user = authService.registerWithRole(
                request.getEmail(),
                request.getPassword(),
                request.getFullName(),
                request.getPhone(),
                request.getAddress(),
                request.getRole(),
                true 
        );

        String token = authService.generateToken(user);

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();

        RegisterResponse response = RegisterResponse.builder()
                .returnCode(ResponseCode.CREATED.getCode())
                .message("Tạo user thành công với role: " + user.getRole())
                .success(true)
                .user(userDTO)
                .token(token)
                .build();

        return ResponseEntity.status(ResponseCode.CREATED.getCode()).body(response);
    }
}
