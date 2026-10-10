package id.manajemenasrama.dto.kegiatan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JadwalKegiatanByDayDto {
    private List<JadwalKegiatanListByDayResponseDto> senin;
    private List<JadwalKegiatanListByDayResponseDto> selasa;
    private List<JadwalKegiatanListByDayResponseDto> rabu;
    private List<JadwalKegiatanListByDayResponseDto> kamis;
    private List<JadwalKegiatanListByDayResponseDto> jumat;
    private List<JadwalKegiatanListByDayResponseDto> sabtu;
    private List<JadwalKegiatanListByDayResponseDto> minggu;
}
