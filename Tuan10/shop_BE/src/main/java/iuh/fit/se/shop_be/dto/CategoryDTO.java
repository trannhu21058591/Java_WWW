package iuh.fit.se.shop_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CategoryDTO {
    private long id;
    private String name;
    private String description;
    
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String imageURL; // URL ảnh danh mục
    
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

