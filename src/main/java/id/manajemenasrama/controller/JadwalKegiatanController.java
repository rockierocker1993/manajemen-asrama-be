package id.manajemenasrama.controller;

import id.manajemenasrama.dto.*;
import id.manajemenasrama.dto.kegiatan.JadwalKegiatanByDayDto;
import id.manajemenasrama.dto.kegiatan.JadwalKegiatanListResponseDto;
import id.manajemenasrama.dto.kegiatan.JadwalKegiatanRequestDto;
import id.manajemenasrama.dto.kegiatan.JadwalKegiatanResponseDto;
import id.manajemenasrama.service.JadwalKegiatanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jadwal-kegiatan")
@RequiredArgsConstructor
public class JadwalKegiatanController {
    private final JadwalKegiatanService jadwalKegiatanService;

    @GetMapping(value = {"/active"})
    public ResponseEntity<BaseResponse<List<JadwalKegiatanListResponseDto>>> getAllActive() {
        return jadwalKegiatanService.list();
    }

    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<List<JadwalKegiatanListResponseDto>>> getAll() {
        return jadwalKegiatanService.list();
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<BaseResponse<JadwalKegiatanResponseDto>> getById(@PathVariable("id") Long id) {
        return jadwalKegiatanService.getById(id);
    }

    @PostMapping(value = {"", "/"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<JadwalKegiatanResponseDto>> create(@Valid @RequestBody JadwalKegiatanRequestDto jadwalKegiatanRequestDto) {
        return jadwalKegiatanService.create(jadwalKegiatanRequestDto);
    }

    @PutMapping(value = {"/{id}"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<JadwalKegiatanResponseDto>> update(@Valid @RequestBody JadwalKegiatanRequestDto jadwalKegiatanRequestDto, @PathVariable("id") Long id) {
        return jadwalKegiatanService.update(jadwalKegiatanRequestDto, id);
    }

    @DeleteMapping(value = {"/{id}"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable("id") Long id) {
        return jadwalKegiatanService.delete(id);
    }

    @PutMapping(value = {"/active-deactive/{id}/{activeDeactive}"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<Void>> activeDeactive(@PathVariable("id") Long id, @PathVariable("activeDeactive") boolean activeDeactive) {
        return jadwalKegiatanService.activeDeactive(id, activeDeactive);
    }

    @GetMapping(value = {"/me/today"})
    public ResponseEntity<BaseResponse<List<JadwalKegiatanListResponseDto>>> meToday() {
        return jadwalKegiatanService.meToday();
    }

    @GetMapping(value = {"/me/by-day"})
    public ResponseEntity<BaseResponse<JadwalKegiatanByDayDto>> meByDay() {
        return jadwalKegiatanService.meByDay();
    }

    @GetMapping(value = {"/asrama/by-day"})
    public ResponseEntity<BaseResponse<JadwalKegiatanByDayDto>> asramaByDay() {
        return jadwalKegiatanService.meByDay();
    }

}
