package id.manajemenasrama.dto.ijin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class IjinRequestDto {

    @NotBlank(message = "Nama santri wajib diisi")
    @Size(max = 100, message = "Nama santri maksimal 100 karakter")
    private String namaSantri;

    @NotNull(message = "Tanggal ijin wajib diisi")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalIjin;

    @NotBlank(message = "Tipe ijin wajib diisi")
    @Pattern(regexp = "(?i)IJIN|SAKIT", message = "Tipe ijin harus IJIN atau SAKIT")
    private String tipeIjin;

    @NotBlank(message = "Alasan wajib diisi")
    @Size(max = 500, message = "Alasan maksimal 500 karakter")
    private String alasan;

}

