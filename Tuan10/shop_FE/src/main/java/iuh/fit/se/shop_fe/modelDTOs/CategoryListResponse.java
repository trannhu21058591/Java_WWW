package iuh.fit.se.shop_fe.modelDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListResponse {
    private int returnCode;
    private String message;
    private boolean success;
    private List<CategoryDTO> categories;
    private long total;
}

