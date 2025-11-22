package iuh.fit.se.shop_fe.services;

import iuh.fit.se.shop_fe.modelDTOs.LoginResponse;
import iuh.fit.se.shop_fe.modelDTOs.UserDTO;

public interface AuthService {
    LoginResponse login(String email, String password);
    LoginResponse register(String email, String password, String fullName, String phone, String address);
    void logout();
    UserDTO getCurrentUser();
    boolean isAuthenticated();
}

