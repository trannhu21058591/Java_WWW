package iuh.fit.se.shop_be.services.Impl;

import iuh.fit.se.shop_be.dto.response.ImageResponse;
import iuh.fit.se.shop_be.services.ImageService;
import iuh.fit.se.shop_be.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Override
    public ImageResponse uploadImage(String base64Image, String imageName) {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("Base64 image không được để trống");
        }
        
        if (!ImageUtil.isValidBase64(base64Image)) {
            throw new IllegalArgumentException("Base64 image không hợp lệ");
        }
        
        String normalizedBase64 = ImageUtil.normalizeBase64(base64Image);
        
        String contentType = ImageUtil.getContentType(normalizedBase64);
        
        byte[] imageBytes = ImageUtil.convertFromBase64(normalizedBase64);
        long size = imageBytes != null ? imageBytes.length : 0;
        
        if (imageName == null || imageName.isEmpty()) {
            String extension = ImageUtil.getExtensionFromContentType(contentType);
            imageName = "image_" + UUID.randomUUID().toString() + "." + extension;
        }
        
        return ImageResponse.builder()
                .base64Image(normalizedBase64)
                .contentType(contentType)
                .size(size)
                .imageName(imageName)
                .build();
    }

    @Override
    public List<ImageResponse> uploadMultipleImages(List<String> base64Images, String prefix) {
        if (base64Images == null || base64Images.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ảnh không được để trống");
        }
        
        List<ImageResponse> responses = new ArrayList<>();
        
        for (int i = 0; i < base64Images.size(); i++) {
            String base64Image = base64Images.get(i);
            String imageName = prefix != null ? prefix + "_" + (i + 1) : null;
            
            try {
                ImageResponse response = uploadImage(base64Image, imageName);
                responses.add(response);
            } catch (Exception e) {
                log.error("Lỗi khi upload ảnh thứ {}: {}", i + 1, e.getMessage());
                // Có thể skip hoặc throw exception tùy yêu cầu
                throw new RuntimeException("Lỗi khi upload ảnh thứ " + (i + 1) + ": " + e.getMessage(), e);
            }
        }
        
        return responses;
    }

    @Override
    public boolean validateBase64Image(String base64Image) {
        return ImageUtil.isValidBase64(base64Image);
    }

    @Override
    public ImageResponse getImageInfo(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }
        
        if (!ImageUtil.isValidBase64(base64Image)) {
            throw new IllegalArgumentException("Base64 image không hợp lệ");
        }
        
        String normalizedBase64 = ImageUtil.normalizeBase64(base64Image);
        String contentType = ImageUtil.getContentType(normalizedBase64);
        byte[] imageBytes = ImageUtil.convertFromBase64(normalizedBase64);
        long size = imageBytes != null ? imageBytes.length : 0;
        
        return ImageResponse.builder()
                .base64Image(normalizedBase64)
                .contentType(contentType)
                .size(size)
                .build();
    }

    @Override
    public String resizeImage(String base64Image, int maxWidth, int maxHeight) {
        log.warn("Resize image chưa được implement, trả về ảnh gốc");
        return base64Image;
    }
}

