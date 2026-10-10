package id.manajemenasrama.controller;

import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.kegiatan.KegiatanListResponseDto;
import id.manajemenasrama.service.KegiatanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kegiatan")
@RequiredArgsConstructor
public class KegiatanController {
    private final KegiatanService kegiatanService;

    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<List<KegiatanListResponseDto>>> getAll() {
        return kegiatanService.list();
    }
}
