package com.example.tuan09.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    private long id;
    private String name;
    private String brand;
    private String modelNumber;
    private String description;
    private double price;
    private int stock;
    private String material;
    private String movement;
    private  String gender;
    private String diaColor;
    private String strapColor;
    private String imageURL;
    private int sold = 0;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private  Category category;

    private List<Review> reviews;
}
