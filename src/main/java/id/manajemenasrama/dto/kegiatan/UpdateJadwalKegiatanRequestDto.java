package id.manajemenasrama.dto.kegiatan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class UpdateJadwalKegiatanRequestDto {

    @NotBlank(message = "Nama is required")
    private String nama;

    @NotBlank(message = "Deskripsi is required")
    private String deskripsi;

    @NotBlank(message = "Kategori is required")
    private String kategori;

    @NotNull(message = "Hari is required")
    private Integer hari;

    @NotNull(message = "Jam Awal is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime jamAwal;

    @NotNull(message = "Jam Akhir is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime jamAkhir;

    @NotNull(message = "Tanggal Awal Efektif is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalAwalEfektif;

    @NotNull(message = "Tanggal Akhir Efektif is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalAkhirEfektif;

    private boolean status = true;

    private String pelaksana;

}
