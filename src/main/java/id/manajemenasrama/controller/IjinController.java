package id.manajemenasrama.controller;

import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.ijin.IjinApprovalRequestDto;
import id.manajemenasrama.dto.ijin.IjinRequestDto;
import id.manajemenasrama.dto.ijin.IjinResponseDto;
import id.manajemenasrama.service.IjinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ijin")
@RequiredArgsConstructor
public class IjinController {

    private final IjinService ijinService;

    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasAnyAuthority('PENGURUS','USTADZAH')")
    public ResponseEntity<BaseResponse<List<IjinResponseDto>>> getAll() {
        return ijinService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PENGURUS','USTADZAH')")
    public ResponseEntity<BaseResponse<IjinResponseDto>> getById(@PathVariable Long id) {
        return ijinService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('PENGURUS','USTADZAH')")
    public ResponseEntity<BaseResponse<IjinResponseDto>> create(@Valid @RequestBody IjinRequestDto request) {
        return ijinService.create(request);
    }

    @PutMapping("/approval/{id}")
    @PreAuthorize("hasAnyAuthority('PENGURUS')")
    public ResponseEntity<BaseResponse<IjinResponseDto>> approval(
            @PathVariable Long id,
            @Valid @RequestBody IjinApprovalRequestDto request) {
        return ijinService.approval(id, request);
    }

}

