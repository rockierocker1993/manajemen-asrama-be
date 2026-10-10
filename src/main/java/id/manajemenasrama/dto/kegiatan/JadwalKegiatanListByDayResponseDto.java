package id.manajemenasrama.dto.kegiatan;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface JadwalKegiatanListByDayResponseDto {
    Long getId();
    String getNama();
    String getKategori();
    String getHari();
    String getJamAwal();
    String getJamAkhir();
    String getTanggalAwalEfektif();
    String getTanggalAkhirEfektif();
    String getJadwalKegiatanUsers();
    String getJadwalKegiatanAsrama();

    String tipeKegiatan();

    String getDeskripsi();
}
