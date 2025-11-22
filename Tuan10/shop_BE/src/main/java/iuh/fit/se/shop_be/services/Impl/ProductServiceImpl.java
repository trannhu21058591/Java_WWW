package iuh.fit.se.shop_be.services.Impl;

import iuh.fit.se.shop_be.entities.Product;
import iuh.fit.se.shop_be.exceptions.ResourceNotFoundException;
import iuh.fit.se.shop_be.repositories.ProductRepository;
import iuh.fit.se.shop_be.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public List<Product> getAllProducts(boolean includeDeleted) {
        if (includeDeleted) {
            return productRepository.findAll();
        }
        return productRepository.findAllActive();
    }
    
    @Override
    public List<Product> getActiveProducts() {
        // Chỉ lấy sản phẩm active và chưa xóa (cho guest/customer)
        return productRepository.findAllActiveAndNotDeleted();
    }
    
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdAndNotDeleted(id);
    }
    
    @Override
    public Optional<Product> getActiveProductById(Long id) {
        // Lấy sản phẩm active theo ID (cho guest/customer)
        return productRepository.findActiveById(id);
    }
    
    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        // Lấy sản phẩm active theo category (cho guest/customer)
        return productRepository.findActiveByCategoryId(categoryId);
    }
    
    @Override
    @Transactional
    public Product createProduct(Product product) {
        product.setDeletedAt(null);
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        
        // Cập nhật thông tin
        existingProduct.setName(product.getName());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setModelNumber(product.getModelNumber());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setMaterial(product.getMaterial());
        existingProduct.setMovement(product.getMovement());
        existingProduct.setGender(product.getGender());
        existingProduct.setDiaColor(product.getDiaColor());
        existingProduct.setStrapColor(product.getStrapColor());
        existingProduct.setCaseSize(product.getCaseSize());
        existingProduct.setWaterResistance(product.getWaterResistance());
        existingProduct.setImageURL(product.getImageURL());
        existingProduct.setImageURLs(product.getImageURLs());
        existingProduct.setActive(product.isActive());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(existingProduct);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // Soft delete - gỡ sản phẩm (không xóa khỏi DB)
        Product product = productRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        
        product.setDeletedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    @Transactional
    public void restoreProduct(Long id) {
        // Khôi phục sản phẩm đã xóa
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        
        if (product.getDeletedAt() == null) {
            throw new IllegalArgumentException("Sản phẩm này chưa bị xóa!");
        }
        
        product.setDeletedAt(null);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    @Transactional
    public void hardDeleteProduct(Long id) {
        // Xóa cứng - chỉ xóa khi chưa có trong đơn hàng nào
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        
        // Kiểm tra xem sản phẩm có trong đơn hàng nào không
        if (productRepository.existsInOrder(id)) {
            throw new IllegalStateException("Không thể xóa sản phẩm này vì đã có trong đơn hàng!");
        }
        
        productRepository.delete(product);
    }
    
    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingAndNotDeleted(keyword);
    }
    
    @Override
    public List<Product> searchActiveProducts(String name, String brand, Double minPrice, Double maxPrice, Long categoryId, String gender, String material) {
        return productRepository.searchActiveProducts(name, brand, minPrice, maxPrice, categoryId, gender, material);
    }
}

