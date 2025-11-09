package com.example.tuan09.entities;

import com.example.tuan09.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt = LocalDateTime.now();



}
