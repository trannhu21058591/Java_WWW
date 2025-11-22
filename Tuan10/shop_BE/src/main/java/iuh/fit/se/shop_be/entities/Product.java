package iuh.fit.se.shop_be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false)
    private String name;
    
    private String brand;
    private String modelNumber;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private double price;
    
    @Column(nullable = false)
    private int stock;
    
    private String material;        // Chất liệu (Thép, vàng, bạc, da, ...)
    private String movement;        // Bộ máy (Quartz, Automatic, Mechanical, ...)
    private String gender;          // Giới tính (Nam, Nữ, Unisex)
    private String diaColor;        // Màu mặt đồng hồ
    private String strapColor;      // Màu dây đeo
    private String caseSize;        // Kích thước vỏ (mm)
    private String waterResistance; // Độ chống nước
    
    @Column(columnDefinition = "LONGTEXT")
    private String imageURL;       
    
    @Column(columnDefinition = "LONGTEXT")
    private String imageURLs;       
    
    private int sold = 0;           // Số lượng đã bán
    
    @Column(name = "is_active")
    private boolean active = true; // Trạng thái hoạt động (để ẩn/hiện sản phẩm)
    
    private LocalDateTime deletedAt; // Thời điểm xóa (soft delete - null = chưa xóa)
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
