package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.*;
import id.manajemenasrama.dto.kegiatan.*;
import id.manajemenasrama.entity.*;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.repository.*;
import id.manajemenasrama.util.CommonUtil;
import id.manajemenasrama.util.DateUtil;
import id.manajemenasrama.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class JadwalKegiatanService {
    private final JadwalKegiatanRepository jadwalKegiatanRepository;
    private final KegiatanRepository kegiatanRepository;
    private final KategoriRepository kategoriRepository;
    private final UserRepository userRepository;
    private final AsramaRepository asramaRepository;

    public ResponseEntity<BaseResponse<List<JadwalKegiatanListResponseDto>>> list() {
        log.info("get list jadwal kegiatan ");
        List<JadwalKegiatanListResponseDto> jadwalKegiatanListResponseDtos = jadwalKegiatanRepository.findAllListByStatus(null);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, jadwalKegiatanListResponseDtos)
        );
    }

    public ResponseEntity<BaseResponse<List<JadwalKegiatanListResponseDto>>> listActive() {
        log.info("get list jadwal kegiatan active ");
        List<JadwalKegiatanListResponseDto> jadwalKegiatanListResponseDtos = jadwalKegiatanRepository.findAllListByStatus(true);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, jadwalKegiatanListResponseDtos)
        );
    }

    public ResponseEntity<BaseResponse<JadwalKegiatanResponseDto>> getById(Long id) {
        log.info("get jadwal kegiatan by id {}", id);
        JadwalKegiatan jadwalKegiatan = jadwalKegiatanRepository.findById(id).orElseThrow(() -> new BadRequestException(ResponseCode.JADWAL_KEGIATAN_NOT_FOUND));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, buildResponse(jadwalKegiatan))
        );
    }

    public ResponseEntity<BaseResponse<JadwalKegiatanResponseDto>> create(JadwalKegiatanRequestDto jadwalKegiatanRequestDto) {
        log.info("create jadwal kegiatan");
        JadwalKegiatan jadwalKegiatan = saveUpdate(jadwalKegiatanRequestDto, null);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.CREATE_SUCCESS, buildResponse(jadwalKegiatan))
        );
    }

    public ResponseEntity<BaseResponse<JadwalKegiatanResponseDto>> update(JadwalKegiatanRequestDto jadwalKegiatanRequestDto, Long id) {
        log.info("update jadwal kegiatan");
        JadwalKegiatan jadwalKegiatan = saveUpdate(jadwalKegiatanRequestDto, id);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.UPDATE_SUCCESS, buildResponse(jadwalKegiatan))
        );
    }

    public ResponseEntity<BaseResponse<Void>> activeDeactive(Long id, boolean activeDeactive) {
        log.info("active deactive jadwal kegiatan {}, {}", id, activeDeactive);
        JadwalKegiatan jadwalKegiatan = jadwalKegiatanRepository.findById(id).orElseThrow(() -> new BadRequestException(ResponseCode.JADWAL_KEGIATAN_NOT_FOUND));
        jadwalKegiatan.setStatus(activeDeactive);
        jadwalKegiatan.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        jadwalKegiatanRepository.save(jadwalKegiatan);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, null)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<BaseResponse<Void>> delete(Long id) {
        log.info("delete jadwal kegiatan");
        JadwalKegiatan jadwalKegiatan = jadwalKegiatanRepository.findById(id).orElseThrow(() -> new BadRequestException(ResponseCode.JADWAL_KEGIATAN_NOT_FOUND));
        jadwalKegiatan.setDeleted(LocalDateTime.now());
        jadwalKegiatan.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
//        if (!jadwalKegiatanRepository.existsByKegiatanAndIdIsNot(jadwalKegiatan.getKegiatan(), id)) {
//            Kegiatan kegiatan = jadwalKegiatan.getKegiatan();
//            kegiatan.setDeleted(LocalDateTime.now());
//            kegiatan.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
//            kegiatanRepository.save(kegiatan);
//        }
        jadwalKegiatanRepository.save(jadwalKegiatan);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.DELETE_SUCCESS, null)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    private JadwalKegiatan saveUpdate(JadwalKegiatanRequestDto jadwalKegiatanRequestDto, Long id) {
        JadwalKegiatan jadwalKegiatan;
        if (Objects.nonNull(id)) {
            jadwalKegiatan = jadwalKegiatanRepository.findById(id).orElseThrow(() -> new BadRequestException(ResponseCode.JADWAL_KEGIATAN_NOT_FOUND));
            jadwalKegiatan.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            jadwalKegiatan = new JadwalKegiatan();
            jadwalKegiatan.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        jadwalKegiatan.setTipeKegiatan(jadwalKegiatanRequestDto.getTipeKegiatan());
        jadwalKegiatan.setHari(String.join(",", jadwalKegiatanRequestDto.getHari()));
        jadwalKegiatan.setJamAwal(jadwalKegiatanRequestDto.getJamAwal());
        jadwalKegiatan.setJamAkhir(jadwalKegiatanRequestDto.getJamAkhir());
        jadwalKegiatan.setTanggalAkhirEfektif(jadwalKegiatanRequestDto.getTanggalAkhirEfektif());
        jadwalKegiatan.setTanggalAwalEfektif(jadwalKegiatanRequestDto.getTanggalAwalEfektif());
        jadwalKegiatan.setStatus(jadwalKegiatanRequestDto.isStatus());
        if (Objects.nonNull(jadwalKegiatanRequestDto.getJadwalKegiatanUserIds()) && !jadwalKegiatanRequestDto.getJadwalKegiatanUserIds().isEmpty()) {
            List<User> users = new ArrayList<>();
            for (Long userId : jadwalKegiatanRequestDto.getJadwalKegiatanUserIds()) {
                final User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BadRequestException(ResponseCode.USER_NOT_FOUND));
                users.add(user);
            }
            jadwalKegiatan.setJadwalKegiatanUsers(users);
        }

        if (Objects.nonNull(jadwalKegiatanRequestDto.getJadwalKegiatanAsramaIds()) && !jadwalKegiatanRequestDto.getJadwalKegiatanAsramaIds().isEmpty()) {
            List<Asrama> asramas = new ArrayList<>();
            for (Long asramaId : jadwalKegiatanRequestDto.getJadwalKegiatanAsramaIds()) {
                final Asrama asrama = asramaRepository.findById(asramaId)
                        .orElseThrow(() -> new BadRequestException(ResponseCode.ASRAMA_NOT_FOUND));
                asramas.add(asrama);
            }
            jadwalKegiatan.setJadwalKegiatanAsrama(asramas);
        }

        Kategori kategori = kategoriRepository.findByNama(jadwalKegiatanRequestDto.getKategori())
                .orElse(null);
        if (Objects.isNull(kategori)) {
            kategori = new Kategori();
            kategori.setNama(jadwalKegiatanRequestDto.getKategori());
            kategori.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            kategori = kategoriRepository.save(kategori);
        }

        Kegiatan kegiatan = kegiatanRepository.findByNamaAndKategori(jadwalKegiatanRequestDto.getNama(), kategori).orElse(null);
        if (Objects.isNull(kegiatan)) {
            kegiatan = new Kegiatan();
            kegiatan.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        }

        kegiatan.setNama(jadwalKegiatanRequestDto.getNama());
        kegiatan.setDeskripsi(jadwalKegiatanRequestDto.getDeskripsi());
        kegiatan.setKategori(kategori);
        kegiatanRepository.save(kegiatan);

        jadwalKegiatan.setKegiatan(kegiatan);
        return jadwalKegiatanRepository.save(jadwalKegiatan);
    }

    private JadwalKegiatanResponseDto buildResponse(JadwalKegiatan jadwalKegiatan) {
        List<JadwalKegiatanUserDto> jadwalKegiatanUserDtos = new ArrayList<>();
        if (Objects.nonNull(jadwalKegiatan.getJadwalKegiatanUsers()) && !jadwalKegiatan.getJadwalKegiatanUsers().isEmpty()) {
            for (User user : jadwalKegiatan.getJadwalKegiatanUsers()) {
                JadwalKegiatanUserDto jadwalKegiatanUserDto = JadwalKegiatanUserDto.builder()
                        .id(user.getId())
                        .role(Objects.nonNull(user.getRole()) ? user.getRole().getName() : null)
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .build();
                jadwalKegiatanUserDtos.add(jadwalKegiatanUserDto);
            }
        }

        List<JadwalKegiatanAsramaDto> jadwalKegiatanAsramaDtos = new ArrayList<>();
        if (Objects.nonNull(jadwalKegiatan.getJadwalKegiatanAsrama()) && !jadwalKegiatan.getJadwalKegiatanAsrama().isEmpty()) {
            for (Asrama asrama : jadwalKegiatan.getJadwalKegiatanAsrama()) {
                JadwalKegiatanAsramaDto jadwalKegiatanAsramaDto = JadwalKegiatanAsramaDto.builder()
                        .id(asrama.getId())
                        .nama(asrama.getNama())
                        .build();
                jadwalKegiatanAsramaDtos.add(jadwalKegiatanAsramaDto);
            }
        }

        return JadwalKegiatanResponseDto.builder()
                .id(jadwalKegiatan.getId())
                .nama(jadwalKegiatan.getKegiatan().getNama())
                .deskripsi(jadwalKegiatan.getKegiatan().getDeskripsi())
                .kategori(jadwalKegiatan.getKegiatan().getKategori().getNama())
                .hari(List.of(jadwalKegiatan.getHari().split(",")))
                .jamAwal(jadwalKegiatan.getJamAwal())
                .jamAkhir(jadwalKegiatan.getJamAkhir())
                .tanggalAwalEfektif(jadwalKegiatan.getTanggalAwalEfektif())
                .tanggalAkhirEfektif(jadwalKegiatan.getTanggalAkhirEfektif())
                .status(jadwalKegiatan.isStatus())
                .tipeKegiatan(jadwalKegiatan.getTipeKegiatan())
                .jadwalKegiatanUsers(jadwalKegiatanUserDtos)
                .jadwalKegiatanAsrama(jadwalKegiatanAsramaDtos)
                .build();
    }

    public ResponseEntity<BaseResponse<List<JadwalKegiatanListResponseDto>>> meToday() {
        log.info("get jadwal kegiatan me today");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JadwalKegiatanListResponseDto> jadwalKegiatanListResponseDtos = jadwalKegiatanRepository.findActiveTodayByUsernameAndHari(username, String.valueOf(DateUtil.getDayOfWeek(LocalDate.now())));
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, jadwalKegiatanListResponseDtos));
    }
}
