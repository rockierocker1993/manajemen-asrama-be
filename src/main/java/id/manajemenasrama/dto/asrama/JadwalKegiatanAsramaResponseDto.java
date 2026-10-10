package id.manajemenasrama.dto.asrama;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JadwalKegiatanAsramaResponseDto {
    private Long id;
    private String nama;
    private String deskripsi;
    private String kategori;
    private Integer hari;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime jamAwal;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime jamAkhir;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalAwalEfektif;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalAkhirEfektif;
    private boolean status = true;
    private String tipeKegiatan;
}
