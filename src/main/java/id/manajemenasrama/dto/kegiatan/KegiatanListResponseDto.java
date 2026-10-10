package id.manajemenasrama.dto.kegiatan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KegiatanListResponseDto {
    private Long id;
    private String nama;
    private String deskripsi;
    private String kategori;
}
