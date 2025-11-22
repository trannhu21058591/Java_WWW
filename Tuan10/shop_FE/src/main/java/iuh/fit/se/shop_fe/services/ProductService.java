package iuh.fit.se.shop_fe.services;

import iuh.fit.se.shop_fe.modelDTOs.ProductDetailResponse;
import iuh.fit.se.shop_fe.modelDTOs.ProductListResponse;

public interface ProductService {
    ProductListResponse getAllProducts();
    ProductListResponse getProductsByCategory(Long categoryId);
    ProductDetailResponse getProductById(Long id);
}
