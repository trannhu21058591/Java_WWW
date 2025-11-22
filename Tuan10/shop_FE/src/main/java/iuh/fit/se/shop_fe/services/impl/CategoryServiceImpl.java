package iuh.fit.se.shop_fe.services.impl;

import iuh.fit.se.shop_fe.modelDTOs.CategoryListResponse;
import iuh.fit.se.shop_fe.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Value("${backend.api.url}")
    private String backendApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public CategoryListResponse getAllCategories() {
        String url = backendApiUrl + "/api/categories";
        return restTemplate.getForObject(url, CategoryListResponse.class);
    }
}

