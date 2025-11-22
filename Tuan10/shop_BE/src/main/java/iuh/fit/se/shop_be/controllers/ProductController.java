package iuh.fit.se.shop_be.controllers;

import iuh.fit.se.shop_be.Enum.ResponseCode;
import iuh.fit.se.shop_be.dto.ProductDTO;
import iuh.fit.se.shop_be.dto.ProductDetailDTO;
import iuh.fit.se.shop_be.dto.request.CreateProductRequest;
import iuh.fit.se.shop_be.dto.request.UpdateProductRequest;
import iuh.fit.se.shop_be.dto.response.ImageResponse;
import iuh.fit.se.shop_be.dto.response.ProductDetailResponse;
import iuh.fit.se.shop_be.dto.response.ProductImageResponse;
import iuh.fit.se.shop_be.dto.response.ProductListResponse;
import iuh.fit.se.shop_be.entities.Category;
import iuh.fit.se.shop_be.entities.Product;
import iuh.fit.se.shop_be.exceptions.ResourceNotFoundException;
import iuh.fit.se.shop_be.repositories.CategoryRepository;
import iuh.fit.se.shop_be.services.ImageService;
import iuh.fit.se.shop_be.services.ProductService;
import iuh.fit.se.shop_be.util.ImageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;

    /**
     * GET /api/products
     * Lấy danh sách sản phẩm (cho guest và customer)
     * Query params:
     *   - category: lọc theo category ID
     *   - name: tìm kiếm theo tên sản phẩm (không phân biệt hoa thường)
     *   - brand: lọc theo thương hiệu
     *   - minPrice: giá tối thiểu
     *   - maxPrice: giá tối đa
     *   - gender: lọc theo giới tính (Nam, Nữ, Unisex)
     *   - material: lọc theo chất liệu
     * Không cần token
     */
    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String material) {
        List<Product> products;
        
        boolean hasSearchParams = name != null || brand != null || minPrice != null || 
                                 maxPrice != null || gender != null || material != null;
        
        if (hasSearchParams || category != null) {
            products = productService.searchActiveProducts(name, brand, minPrice, maxPrice, category, gender, material);
        } else {
            products = productService.getActiveProducts();
        }
        
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        String message = "Lấy danh sách sản phẩm thành công!";
        if (hasSearchParams || category != null) {
            message = "Tìm kiếm sản phẩm thành công!";
        }
        
        ProductListResponse response = ProductListResponse.builder()
                .returnCode(ResponseCode.SUCCESS.getCode())
                .message(message)
                .success(true)
                .products(productDTOs)
                .total(productDTOs.size())
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/{id}
     * Lấy chi tiết sản phẩm theo ID (cho guest và customer)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getActiveProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        
        ProductDetailDTO productDetailDTO = convertToDetailDTO(product);
        
        ProductDetailResponse response = ProductDetailResponse.builder()
                .returnCode(ResponseCode.SUCCESS.getCode())
                .message("Lấy chi tiết sản phẩm thành công!")
                .success(true)
                .product(productDetailDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/{id}/image
     * Lấy ảnh chính của sản phẩm dưới dạng base64
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<ProductImageResponse> getProductImage(@PathVariable Long id) {
        try {
            Product product = productService.getActiveProductById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
            
            String imageURL = product.getImageURL();
            if (imageURL == null || imageURL.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ProductImageResponse.builder()
                                .returnCode(ResponseCode.NOT_FOUND.getCode())
                                .message("Sản phẩm không có ảnh")
                                .success(false)
                                .build());
            }
            
            ImageResponse imageResponse;
            if (ImageUtil.isValidBase64(imageURL)) {
                imageResponse = imageService.getImageInfo(imageURL);
            } else {
                imageResponse = ImageResponse.builder()
                        .base64Image(imageURL) 
                        .contentType("image/png") 
                        .imageName("product_" + id + "_image")
                        .build();
            }
            
            ProductImageResponse response = ProductImageResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Lấy ảnh sản phẩm thành công!")
                    .success(true)
                    .image(imageResponse)
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProductImageResponse.builder()
                            .returnCode(ResponseCode.NOT_FOUND.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi lấy ảnh sản phẩm: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ProductImageResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi lấy ảnh sản phẩm: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * POST /api/products/admin/create
     * Tạo mới sản phẩm (chỉ dành cho ADMIN)
     * Cần token với role ADMIN
     */
    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDetailResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        try {

            Category category = categoryRepository.findByIdAndNotDeleted(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy danh mục với ID: " + request.getCategoryId()));

            Product product = Product.builder()
                    .name(request.getName())
                    .brand(request.getBrand())
                    .modelNumber(request.getModelNumber())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .stock(request.getStock())
                    .material(request.getMaterial())
                    .movement(request.getMovement())
                    .gender(request.getGender())
                    .diaColor(request.getDiaColor())
                    .strapColor(request.getStrapColor())
                    .caseSize(request.getCaseSize())
                    .waterResistance(request.getWaterResistance())
                    .imageURL(request.getImageURL())
                    .imageURLs(request.getImageURLs())
                    .active(request.getActive() != null ? request.getActive() : true)
                    .category(category)
                    .sold(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(null)
                    .build();

            Product savedProduct = productService.createProduct(product);

            ProductDetailDTO productDetailDTO = convertToDetailDTO(savedProduct);

            ProductDetailResponse response = ProductDetailResponse.builder()
                    .returnCode(ResponseCode.CREATED.getCode())
                    .message("Tạo sản phẩm thành công!")
                    .success(true)
                    .product(productDetailDTO)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceNotFoundException e) {
            log.error("Lỗi khi tạo sản phẩm: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProductDetailResponse.builder()
                            .returnCode(ResponseCode.NOT_FOUND.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi tạo sản phẩm: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ProductDetailResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi tạo sản phẩm: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * PUT /api/products/admin/update
     * Cập nhật sản phẩm (chỉ dành cho ADMIN)
     * Cần token với role ADMIN
     */
    @PutMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDetailResponse> updateProduct(
            @Valid @RequestBody UpdateProductRequest request) {
        try {
            Product existingProduct = productService.getProductById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy sản phẩm với ID: " + request.getId()));

            Category category = null;
            if (request.getCategoryId() != null) {
                category = categoryRepository.findByIdAndNotDeleted(request.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Không tìm thấy danh mục với ID: " + request.getCategoryId()));
            } else {
                category = existingProduct.getCategory();
            }

            Product productToUpdate = Product.builder()
                    .name(request.getName() != null ? request.getName() : existingProduct.getName())
                    .brand(request.getBrand() != null ? request.getBrand() : existingProduct.getBrand())
                    .modelNumber(request.getModelNumber() != null ? request.getModelNumber() : existingProduct.getModelNumber())
                    .description(request.getDescription() != null ? request.getDescription() : existingProduct.getDescription())
                    .price(request.getPrice() != null ? request.getPrice() : existingProduct.getPrice())
                    .stock(request.getStock() != null ? request.getStock() : existingProduct.getStock())
                    .material(request.getMaterial() != null ? request.getMaterial() : existingProduct.getMaterial())
                    .movement(request.getMovement() != null ? request.getMovement() : existingProduct.getMovement())
                    .gender(request.getGender() != null ? request.getGender() : existingProduct.getGender())
                    .diaColor(request.getDiaColor() != null ? request.getDiaColor() : existingProduct.getDiaColor())
                    .strapColor(request.getStrapColor() != null ? request.getStrapColor() : existingProduct.getStrapColor())
                    .caseSize(request.getCaseSize() != null ? request.getCaseSize() : existingProduct.getCaseSize())
                    .waterResistance(request.getWaterResistance() != null ? request.getWaterResistance() : existingProduct.getWaterResistance())
                    .imageURL(request.getImageURL() != null ? request.getImageURL() : existingProduct.getImageURL())
                    .imageURLs(request.getImageURLs() != null ? request.getImageURLs() : existingProduct.getImageURLs())
                    .active(request.getActive() != null ? request.getActive() : existingProduct.isActive())
                    .category(category)
                    .sold(existingProduct.getSold())
                    .build();

            Product updatedProduct = productService.updateProduct(request.getId(), productToUpdate);

            ProductDetailDTO productDetailDTO = convertToDetailDTO(updatedProduct);

            ProductDetailResponse response = ProductDetailResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Cập nhật sản phẩm thành công!")
                    .success(true)
                    .product(productDetailDTO)
                    .build();

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.error("Lỗi khi cập nhật sản phẩm: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProductDetailResponse.builder()
                            .returnCode(ResponseCode.NOT_FOUND.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật sản phẩm: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ProductDetailResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi cập nhật sản phẩm: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * GET /api/products/{id}/images
     * Lấy tất cả ảnh của sản phẩm (ảnh chính + ảnh phụ) dưới dạng base64
     */
    @GetMapping("/{id}/images")
    public ResponseEntity<ProductImageResponse> getProductImages(@PathVariable Long id) {
        try {
            Product product = productService.getActiveProductById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
            
            List<ImageResponse> allImages = new ArrayList<>();
            
            // Lấy ảnh chính
            String mainImageURL = product.getImageURL();
            if (mainImageURL != null && !mainImageURL.isEmpty()) {
                ImageResponse mainImage;
                if (ImageUtil.isValidBase64(mainImageURL)) {
                    mainImage = imageService.getImageInfo(mainImageURL);
                } else {
                    mainImage = ImageResponse.builder()
                            .base64Image(mainImageURL)
                            .contentType("image/png")
                            .imageName("product_" + id + "_main")
                            .build();
                }
                allImages.add(mainImage);
            }
            
            // Lấy ảnh phụ
            String imageURLs = product.getImageURLs();
            if (imageURLs != null && !imageURLs.isEmpty()) {
                List<String> additionalImages = ImageUtil.parseImageURLs(imageURLs);
                for (int i = 0; i < additionalImages.size(); i++) {
                    String imageURL = additionalImages.get(i);
                    ImageResponse imageResponse;
                    if (ImageUtil.isValidBase64(imageURL)) {
                        imageResponse = imageService.getImageInfo(imageURL);
                    } else {
                        imageResponse = ImageResponse.builder()
                                .base64Image(imageURL)
                                .contentType("image/png")
                                .imageName("product_" + id + "_" + (i + 1))
                                .build();
                    }
                    allImages.add(imageResponse);
                }
            }
            
            if (allImages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ProductImageResponse.builder()
                                .returnCode(ResponseCode.NOT_FOUND.getCode())
                                .message("Sản phẩm không có ảnh")
                                .success(false)
                                .build());
            }
            
            ProductImageResponse response = ProductImageResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Lấy " + allImages.size() + " ảnh sản phẩm thành công!")
                    .success(true)
                    .image(allImages.isEmpty() ? null : allImages.get(0)) // Ảnh chính
                    .images(allImages.size() > 1 ? allImages.subList(1, allImages.size()) : new ArrayList<>()) // Ảnh phụ
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProductImageResponse.builder()
                            .returnCode(ResponseCode.NOT_FOUND.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi lấy ảnh sản phẩm: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ProductImageResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi lấy ảnh sản phẩm: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Convert Product entity to ProductDTO (cho danh sách - thông tin cơ bản)
     */
    private ProductDTO convertToDTO(Product product) {
        String imageURL = product.getImageURL();

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageURL(imageURL)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .gender(product.getGender())
                .build();
    }

    /**
     * Convert Product entity to ProductDetailDTO (cho chi tiết - đầy đủ thông tin)
     */
    private ProductDetailDTO convertToDetailDTO(Product product) {
        return ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .modelNumber(product.getModelNumber())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .material(product.getMaterial())
                .movement(product.getMovement())
                .gender(product.getGender())
                .diaColor(product.getDiaColor())
                .strapColor(product.getStrapColor())
                .caseSize(product.getCaseSize())
                .waterResistance(product.getWaterResistance())
                .imageURL(product.getImageURL())
                .sold(product.getSold())
                .active(product.isActive())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}

