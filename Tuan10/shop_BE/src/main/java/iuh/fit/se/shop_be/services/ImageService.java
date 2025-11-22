package iuh.fit.se.shop_be.services;

import iuh.fit.se.shop_be.dto.response.ImageResponse;

import java.util.List;

public interface ImageService {
    /**
     * Xử lý upload ảnh base64 đơn
     */
    ImageResponse uploadImage(String base64Image, String imageName);
    
    /**
     * Xử lý upload nhiều ảnh base64
     */
    List<ImageResponse> uploadMultipleImages(List<String> base64Images, String prefix);
    
    /**
     * Validate base64 image
     */
    boolean validateBase64Image(String base64Image);
    
    /**
     * Lấy thông tin ảnh từ base64 string
     */
    ImageResponse getImageInfo(String base64Image);
    
    /**
     * Resize ảnh (nếu cần)
     */
    String resizeImage(String base64Image, int maxWidth, int maxHeight);
}

