package iuh.fit.se.shop_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private ImageResponse image;           // Ảnh đã upload
    private List<ImageResponse> images;    // Danh sách ảnh (nếu upload nhiều)
}

