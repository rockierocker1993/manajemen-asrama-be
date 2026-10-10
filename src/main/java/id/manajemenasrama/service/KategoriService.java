package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.entity.Kategori;
import id.manajemenasrama.repository.KategoriRepository;
import id.manajemenasrama.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public ResponseEntity<BaseResponse<List<String>>> list() {
        log.info("get list kategori");
        List<String> kategori = new ArrayList<>();
        List<Kategori> kategoriList = kategoriRepository.findAll();
        for (Kategori k : kategoriList) {
            kategori.add(k.getNama());
        }
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, kategori)
        );
    }

}
