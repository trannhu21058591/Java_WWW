package iuh.fit.se.shop_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    private String base64Image;    // Base64 string của ảnh
    private String contentType;   // Content type (image/png, image/jpeg, ...)
    private long size;             // Kích thước file (bytes)
    private String imageName;      // Tên ảnh (nếu có)
}

