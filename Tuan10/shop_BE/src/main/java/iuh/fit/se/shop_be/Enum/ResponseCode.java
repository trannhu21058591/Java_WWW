package iuh.fit.se.shop_be.Enum;

import lombok.Getter;

@Getter
public enum ResponseCode {
    // Success codes (2xx)
    SUCCESS(200, "Thành công"),
    CREATED(201, "Tạo mới thành công"),
    
    // Client error codes (4xx)
    BAD_REQUEST(400, "Yêu cầu không hợp lệ"),
    UNAUTHORIZED(401, "Chưa xác thực"),
    FORBIDDEN(403, "Không có quyền truy cập"),
    NOT_FOUND(404, "Không tìm thấy"),
    CONFLICT(409, "Xung đột dữ liệu"),
    VALIDATION_ERROR(422, "Lỗi validation"),
    
    // Server error codes (5xx)
    INTERNAL_SERVER_ERROR(500, "Lỗi server"),
    SERVICE_UNAVAILABLE(503, "Dịch vụ không khả dụng");
    
    private final int code;
    private final String message;
    
    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

