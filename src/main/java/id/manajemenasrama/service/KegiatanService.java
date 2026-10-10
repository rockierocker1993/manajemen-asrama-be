package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.kegiatan.KegiatanListResponseDto;
import id.manajemenasrama.repository.KegiatanRepository;
import id.manajemenasrama.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KegiatanService {
    private final KegiatanRepository kegiatanRepository;

    public ResponseEntity<BaseResponse<List<KegiatanListResponseDto>>> list() {
        log.info("get list kegiatan");
        List<KegiatanListResponseDto> kegiatanListResponseDtos = kegiatanRepository.findAllKegiatan();
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, kegiatanListResponseDtos)
        );
    }

}
