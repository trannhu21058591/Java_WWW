package iuh.fit.se.shop_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleImageUploadRequest {
    private List<String> base64Images; 
    private String prefix;              
}

