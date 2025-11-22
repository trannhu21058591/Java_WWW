package iuh.fit.se.shop_be.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

public class ImageUtil {

    private static final Pattern BASE64_PATTERN = Pattern.compile(
            "^data:image/(png|jpg|jpeg|gif|webp|bmp);base64,[A-Za-z0-9+/=]+$"
    );

    private static final Pattern BASE64_DATA_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+/=]+$"
    );

    /**
     * Chuyển đổi MultipartFile sang base64 string
     */
    public static String convertToBase64(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }
            
            byte[] bytes = file.getBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            String contentType = file.getContentType();
            
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("File phải là ảnh");
            }
            
            return "data:" + contentType + ";base64," + base64;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi chuyển đổi file sang base64: " + e.getMessage(), e);
        }
    }

    /**
     * Chuyển đổi byte array sang base64 string với content type
     */
    public static String convertToBase64(byte[] imageBytes, String contentType) {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        return "data:" + contentType + ";base64," + base64;
    }

    /**
     * Chuyển đổi base64 string sang byte array
     */
    public static byte[] convertFromBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        
        try {
            // Nếu có prefix data:image/...;base64, thì loại bỏ
            String base64Data = extractBase64Data(base64String);
            return Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base64 string không hợp lệ: " + e.getMessage(), e);
        }
    }

    /**
     * Lấy content type từ base64 string
     */
    public static String getContentType(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        
        if (base64String.startsWith("data:")) {
            int semicolonIndex = base64String.indexOf(';');
            if (semicolonIndex > 0) {
                return base64String.substring(5, semicolonIndex);
            }
        }
        
        return "image/png"; // Default
    }

    /**
     * Trích xuất phần base64 data từ string (loại bỏ prefix)
     */
    public static String extractBase64Data(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        
        if (base64String.contains(",")) {
            return base64String.substring(base64String.indexOf(",") + 1);
        }
        
        return base64String;
    }

    /**
     * Kiểm tra base64 string có hợp lệ không
     */
    public static boolean isValidBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return false;
        }
        
        // Kiểm tra format đầy đủ: data:image/...;base64,...
        if (base64String.startsWith("data:")) {
            return BASE64_PATTERN.matcher(base64String).matches();
        }
        
        // Kiểm tra chỉ có base64 data
        return BASE64_DATA_PATTERN.matcher(base64String).matches();
    }

    /**
     * Kiểm tra file có phải là ảnh không
     */
    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * Lấy extension từ content type
     */
    public static String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return "png";
        }
        
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
            case "image/jpg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            case "image/webp":
                return "webp";
            case "image/bmp":
                return "bmp";
            default:
                return "png";
        }
    }

    /**
     * Validate và chuẩn hóa base64 string
     */
    public static String normalizeBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        
        // Nếu đã có prefix, giữ nguyên
        if (base64String.startsWith("data:")) {
            if (isValidBase64(base64String)) {
                return base64String;
            }
            throw new IllegalArgumentException("Base64 string không hợp lệ");
        }
        
        // Nếu chỉ có data, thêm prefix mặc định
        if (isValidBase64(base64String)) {
            return "data:image/png;base64," + base64String;
        }
        
        throw new IllegalArgumentException("Base64 string không hợp lệ");
    }

    public static List<String> parseImageURLs(String imageURLs) {
        List<String> result = new ArrayList<>();
        
        if (imageURLs == null || imageURLs.trim().isEmpty()) {
            return result;
        }
        
        String trimmed = imageURLs.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            try {
                // Loại bỏ dấu ngoặc và split
                String content = trimmed.substring(1, trimmed.length() - 1);
                String[] parts = content.split(",");
                for (String part : parts) {
                    String cleaned = part.trim()
                            .replaceAll("^\"|\"$", "") 
                            .replaceAll("^'|'$", ""); 
                    if (!cleaned.isEmpty()) {
                        result.add(cleaned);
                    }
                }
            } catch (Exception e) {
            }
        }
        
        if (result.isEmpty()) {
            String[] parts = trimmed.split(",");
            for (String part : parts) {
                String cleaned = part.trim();
                if (!cleaned.isEmpty()) {
                    result.add(cleaned);
                }
            }
        }
        
        return result;
    }
}

