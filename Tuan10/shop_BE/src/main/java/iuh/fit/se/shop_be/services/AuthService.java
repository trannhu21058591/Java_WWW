package iuh.fit.se.shop_be.services;

import iuh.fit.se.shop_be.Enum.Role;
import iuh.fit.se.shop_be.entities.User;

public interface AuthService {
    User login(String email, String password);
    User register(String email, String password, String fullName, String phone, String address);
    User registerWithRole(String email, String password, String fullName, String phone, String address, Role role, boolean allowAdmin);
    String generateToken(User user);
    String generateRefreshToken(User user);
    String refreshToken(String refreshToken);
}
