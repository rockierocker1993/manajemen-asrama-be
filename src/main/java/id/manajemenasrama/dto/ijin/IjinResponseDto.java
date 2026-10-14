package id.manajemenasrama.dto.ijin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IjinResponseDto {
    private Long id;
    private String namaSantri;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalIjin;

    private String tipeIjin;
    private String alasan;
    private String status;
}

