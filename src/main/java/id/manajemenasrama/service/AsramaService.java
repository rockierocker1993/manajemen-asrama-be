package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.asrama.AsramaRequestDto;
import id.manajemenasrama.dto.asrama.AsramaResponseDto;
import id.manajemenasrama.entity.Asrama;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.repository.AsramaRepository;
import id.manajemenasrama.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsramaService {

	private final AsramaRepository asramaRepository;

	public ResponseEntity<BaseResponse<List<AsramaResponseDto>>> list() {
		log.info("get list asrama");
		List<AsramaResponseDto> data = asramaRepository.findAll(Sort.by(Sort.Direction.ASC, "nama"))
				.stream()
				.map(this::toResponse)
				.toList();
		return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, data));
	}

	public ResponseEntity<BaseResponse<AsramaResponseDto>> getById(Long id) {
		log.info("get asrama by id {}", id);
		Asrama asrama = findAsramaById(id);
		return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, toResponse(asrama)));
	}

	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<BaseResponse<AsramaResponseDto>> create(AsramaRequestDto request) {
		log.info("create asrama {}", request.getNama());
		String nama = request.getNama().trim();
		if (asramaRepository.findByNamaIgnoreCase(nama).isPresent()) {
			throw new BadRequestException(ResponseCode.ASRAMA_ALREADY_EXIST);
		}

		Asrama asrama = Asrama.builder()
				.nama(nama)
				.build();
		asramaRepository.save(asrama);
		return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.CREATE_SUCCESS, toResponse(asrama)));
	}

	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<BaseResponse<AsramaResponseDto>> update(Long id, AsramaRequestDto request) {
		log.info("update asrama {}", id);
		Asrama asrama = findAsramaById(id);
		String nama = request.getNama().trim();
		if (asramaRepository.existsByNamaIgnoreCaseAndIdIsNot(nama, id)) {
			throw new BadRequestException(ResponseCode.ASRAMA_ALREADY_EXIST);
		}

		asrama.setNama(nama);
		asramaRepository.save(asrama);
		return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.UPDATE_SUCCESS, toResponse(asrama)));
	}

	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<BaseResponse<Void>> delete(Long id) {
		log.info("delete asrama {}", id);
		Asrama asrama = findAsramaById(id);
		asrama.setDeleted(LocalDateTime.now());
		asramaRepository.save(asrama);
		return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.DELETE_SUCCESS, null));
	}

	private Asrama findAsramaById(Long id) {
		return asramaRepository.findById(id)
				.orElseThrow(() -> new BadRequestException(ResponseCode.ASRAMA_NOT_FOUND));
	}

	private AsramaResponseDto toResponse(Asrama asrama) {
		return AsramaResponseDto.builder()
				.id(asrama.getId())
				.nama(asrama.getNama())
				.build();
	}
}
