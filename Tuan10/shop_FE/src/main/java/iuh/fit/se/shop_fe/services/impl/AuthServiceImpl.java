package iuh.fit.se.shop_fe.services.impl;

import iuh.fit.se.shop_fe.modelDTOs.LoginResponse;
import iuh.fit.se.shop_fe.modelDTOs.UserDTO;
import iuh.fit.se.shop_fe.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${backend.api.url}")
    private String backendApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public LoginResponse login(String email, String password) {
        try {
            String url = backendApiUrl + "/api/auth/login";
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                    url, 
                    request, 
                    LoginResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Lỗi khi đăng nhập: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi đăng nhập: " + e.getMessage(), e);
        }
    }

    @Override
    public LoginResponse register(String email, String password, String fullName, String phone, String address) {
        try {
            String url = backendApiUrl + "/api/auth/register";
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("fullName", fullName);
            requestBody.put("phone", phone != null ? phone : "");
            requestBody.put("address", address != null ? address : "");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                    url, 
                    request, 
                    LoginResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Lỗi khi đăng ký: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi đăng ký: " + e.getMessage(), e);
        }
    }

    @Override
    public void logout() {
        // Xóa token khỏi session/localStorage
        // Có thể gọi API logout nếu backend có endpoint
    }

    @Override
    public UserDTO getCurrentUser() {
        // Lấy user từ session
        // Sẽ được implement sau khi có session management
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        // Kiểm tra xem user đã đăng nhập chưa
        // Sẽ được implement sau khi có session management
        return false;
    }
}

