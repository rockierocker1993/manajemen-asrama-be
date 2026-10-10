package id.manajemenasrama.dto.kegiatan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import java.time.LocalTime;

public interface JadwalKegiatanListResponseDto {
    Long getId();
    String getNama();
    String getKategori();
    Integer getHari();
    String getJamAwal();
    String getJamAkhir();
    String getTanggalAwalEfektif();
    String getTanggalAkhirEfektif();
    boolean getStatus();
    String gtJadwalKegiatanUsers();
    String gtJadwalKegiatanAsrama();
}
