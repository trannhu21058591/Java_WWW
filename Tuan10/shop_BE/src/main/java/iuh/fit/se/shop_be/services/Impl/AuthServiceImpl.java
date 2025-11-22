package iuh.fit.se.shop_be.services.Impl;

import iuh.fit.se.shop_be.Enum.Role;
import iuh.fit.se.shop_be.entities.User;
import iuh.fit.se.shop_be.exceptions.ResourceNotFoundException;
import iuh.fit.se.shop_be.repositories.UserRepository;
import iuh.fit.se.shop_be.services.AuthService;
import iuh.fit.se.shop_be.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email không tồn tại!"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("Tài khoản đã bị khóa!");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Sai mật khẩu!");
        }

        return user;
    }

    @Override
    @Transactional
    public User register(String email, String password, String fullName, String phone, String address) {
        return registerWithRole(email, password, fullName, phone, address, Role.CUSTOMER, false);
    }

    @Override
    @Transactional
    public User registerWithRole(String email, String password, String fullName, String phone, String address, Role role, boolean allowAdmin) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        if (role == Role.ADMIN && !allowAdmin) {
            throw new IllegalArgumentException("Không thể đăng ký tài khoản ADMIN từ endpoint công khai!");
        }

        String encodedPassword = passwordEncoder.encode(password);

        Role finalRole = role != null ? role : Role.CUSTOMER;

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .fullName(fullName)
                .phone(phone)
                .address(address)
                .role(finalRole)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Override
    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());
    }

    @Override
    public String generateRefreshToken(User user) {
        return jwtUtil.generateRefreshToken(user.getEmail());
    }

    @Override
    public String refreshToken(String refreshToken) {
        try {
            String email = jwtUtil.extractEmail(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại!"));

            if (jwtUtil.validateToken(refreshToken, email)) {
                return generateToken(user);
            } else {
                throw new IllegalArgumentException("Refresh token không hợp lệ hoặc đã hết hạn!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Refresh token không hợp lệ!");
        }
    }
}
