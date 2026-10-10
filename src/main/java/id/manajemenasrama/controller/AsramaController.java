package id.manajemenasrama.controller;

import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.asrama.AsramaRequestDto;
import id.manajemenasrama.dto.asrama.AsramaResponseDto;
import id.manajemenasrama.service.AsramaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/asrama")
@RequiredArgsConstructor
public class AsramaController {

	private final AsramaService asramaService;

	@GetMapping(value = {"", "/"})
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
	public ResponseEntity<BaseResponse<List<AsramaResponseDto>>> getAll() {
		return asramaService.list();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
	public ResponseEntity<BaseResponse<AsramaResponseDto>> getById(@PathVariable Long id) {
		return asramaService.getById(id);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
	public ResponseEntity<BaseResponse<AsramaResponseDto>> create(@Valid @RequestBody AsramaRequestDto request) {
		return asramaService.create(request);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
	public ResponseEntity<BaseResponse<AsramaResponseDto>> update(
			@PathVariable Long id,
			@Valid @RequestBody AsramaRequestDto request) {
		return asramaService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
	public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
		return asramaService.delete(id);
	}

}
