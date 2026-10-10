package id.manajemenasrama.dto.asrama;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsramaRequestDto {

    @NotBlank(message = "Nama asrama wajib diisi")
    @Size(max = 40, message = "Nama asrama maksimal 40 karakter")
    private String nama;
}

