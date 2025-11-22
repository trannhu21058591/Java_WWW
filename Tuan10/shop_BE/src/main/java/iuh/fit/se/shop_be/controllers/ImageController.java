package iuh.fit.se.shop_be.controllers;

import iuh.fit.se.shop_be.Enum.ResponseCode;
import iuh.fit.se.shop_be.dto.request.ImageUploadRequest;
import iuh.fit.se.shop_be.dto.request.MultipleImageUploadRequest;
import iuh.fit.se.shop_be.dto.response.ImageResponse;
import iuh.fit.se.shop_be.dto.response.ImageUploadResponse;
import iuh.fit.se.shop_be.services.ImageService;
import iuh.fit.se.shop_be.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestBody ImageUploadRequest request) {
        try {
            String base64Image = request.getBase64Image();
            if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ImageUploadResponse.builder()
                            .returnCode(ResponseCode.BAD_REQUEST.getCode())
                            .message("Base64 image không được để trống")
                            .success(false)
                            .build());
            }

            ImageResponse imageResponse = imageService.uploadImage(
                    base64Image,
                    request.getImageName()
            );

            ImageUploadResponse response = ImageUploadResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Upload ảnh thành công!")
                    .success(true)
                    .image(imageResponse)
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Lỗi validation: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ImageUploadResponse.builder()
                            .returnCode(ResponseCode.BAD_REQUEST.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi upload ảnh: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImageUploadResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi upload ảnh: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<ImageUploadResponse> uploadMultipleImages(
            @RequestBody MultipleImageUploadRequest request) {
        try {
            List<String> base64Images = request.getBase64Images();
            if (base64Images == null || base64Images.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ImageUploadResponse.builder()
                                .returnCode(ResponseCode.BAD_REQUEST.getCode())
                                .message("Danh sách ảnh không được để trống")
                                .success(false)
                                .build());
            }

            List<ImageResponse> imageResponses = imageService.uploadMultipleImages(
                    base64Images,
                    request.getPrefix()
            );

            ImageUploadResponse response = ImageUploadResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Upload " + imageResponses.size() + " ảnh thành công!")
                    .success(true)
                    .images(imageResponses)
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Lỗi validation: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ImageUploadResponse.builder()
                            .returnCode(ResponseCode.BAD_REQUEST.getCode())
                            .message(e.getMessage())
                            .success(false)
                            .build());
        } catch (Exception e) {
            log.error("Lỗi khi upload nhiều ảnh: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImageUploadResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi upload ảnh: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    @PostMapping("/upload-file")
    public ResponseEntity<ImageUploadResponse> uploadImageFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "imageName", required = false) String imageName) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ImageUploadResponse.builder()
                                .returnCode(ResponseCode.BAD_REQUEST.getCode())
                                .message("File không được để trống")
                                .success(false)
                                .build());
            }

            if (!ImageUtil.isImageFile(file)) {
                return ResponseEntity.badRequest()
                        .body(ImageUploadResponse.builder()
                                .returnCode(ResponseCode.BAD_REQUEST.getCode())
                                .message("File phải là ảnh")
                                .success(false)
                                .build());
            }

            String base64Image = ImageUtil.convertToBase64(file);
            
            ImageResponse imageResponse = imageService.uploadImage(
                    base64Image,
                    imageName != null ? imageName : file.getOriginalFilename()
            );

            ImageUploadResponse response = ImageUploadResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Upload ảnh thành công!")
                    .success(true)
                    .image(imageResponse)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Lỗi khi upload file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImageUploadResponse.builder()
                            .returnCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                            .message("Lỗi khi upload file: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validateImage(@RequestBody ImageUploadRequest request) {
        try {
            String base64Image = request.getBase64Image();
            if (base64Image == null || base64Image.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(ResponseCode.BAD_REQUEST.getCode(), "Base64 image không được để trống"));
            }

            boolean isValid = imageService.validateBase64Image(base64Image);
            
            return ResponseEntity.ok(new ValidationResponse(
                    ResponseCode.SUCCESS.getCode(),
                    isValid ? "Base64 image hợp lệ" : "Base64 image không hợp lệ",
                    isValid
            ));
        } catch (Exception e) {
            log.error("Lỗi khi validate: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), "Lỗi khi validate: " + e.getMessage()));
        }
    }

    @PostMapping("/info")
    public ResponseEntity<?> getImageInfo(@RequestBody ImageUploadRequest request) {
        try {
            String base64Image = request.getBase64Image();
            if (base64Image == null || base64Image.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(ResponseCode.BAD_REQUEST.getCode(), "Base64 image không được để trống"));
            }

            ImageResponse imageInfo = imageService.getImageInfo(base64Image);
            
            return ResponseEntity.ok(ImageUploadResponse.builder()
                    .returnCode(ResponseCode.SUCCESS.getCode())
                    .message("Lấy thông tin ảnh thành công!")
                    .success(true)
                    .image(imageInfo)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ResponseCode.BAD_REQUEST.getCode(), e.getMessage()));
        } catch (Exception e) {
            log.error("Lỗi khi lấy thông tin ảnh: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), "Lỗi khi lấy thông tin ảnh: " + e.getMessage()));
        }
    }

    private static class ErrorResponse {
        private int returnCode;
        private String message;

        public ErrorResponse(int returnCode, String message) {
            this.returnCode = returnCode;
            this.message = message;
        }

        public int getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(int returnCode) {
            this.returnCode = returnCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private static class ValidationResponse {
        private int returnCode;
        private String message;
        private boolean valid;

        public ValidationResponse(int returnCode, String message, boolean valid) {
            this.returnCode = returnCode;
            this.message = message;
            this.valid = valid;
        }

        public int getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(int returnCode) {
            this.returnCode = returnCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }
}

