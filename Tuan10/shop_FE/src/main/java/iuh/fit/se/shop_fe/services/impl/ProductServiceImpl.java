package iuh.fit.se.shop_fe.services.impl;

import iuh.fit.se.shop_fe.modelDTOs.ProductDetailResponse;
import iuh.fit.se.shop_fe.modelDTOs.ProductListResponse;
import iuh.fit.se.shop_fe.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${backend.api.url}")
    private String backendApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public ProductListResponse getAllProducts() {
        String url = backendApiUrl + "/api/products";
        return restTemplate.getForObject(url, ProductListResponse.class);
    }

    @Override
    public ProductListResponse getProductsByCategory(Long categoryId) {
        String url = backendApiUrl + "/api/products?category=" + categoryId;
        return restTemplate.getForObject(url, ProductListResponse.class);
    }

    @Override
    public ProductDetailResponse getProductById(Long id) {
        String url = backendApiUrl + "/api/products/" + id;
        return restTemplate.getForObject(url, ProductDetailResponse.class);
    }
}

