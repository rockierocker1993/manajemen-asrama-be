package id.manajemenasrama.controller;

import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.service.KategoriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kategori")
@RequiredArgsConstructor
public class KategoriController {
    private final KategoriService kategoriService;

    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<List<String>>> getAll() {
        return kategoriService.list();
    }
}
