package id.manajemenasrama.dto.kegiatan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class UpdateUserRequestDto {
    private String username;
    private String role;
    private String fullName;
    private String password;
}