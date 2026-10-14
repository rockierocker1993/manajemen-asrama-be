package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.ijin.IjinApprovalRequestDto;
import id.manajemenasrama.dto.ijin.IjinRequestDto;
import id.manajemenasrama.dto.ijin.IjinResponseDto;
import id.manajemenasrama.entity.Ijin;
import id.manajemenasrama.entity.User;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.repository.IjinRepository;
import id.manajemenasrama.repository.UserRepository;
import id.manajemenasrama.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IjinService {

    private final IjinRepository ijinRepository;
    private final UserRepository userRepository;

    public ResponseEntity<BaseResponse<List<IjinResponseDto>>> list(
            LocalDate tanggalIjinFrom,
            LocalDate tanggalIjinTo,
            String tipeIjin,
            String status,
            String namaSantri
    ) {
        log.info("get list ijin");
        Specification<Ijin> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tanggalIjinFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("tanggalIjin"), tanggalIjinFrom));
            }
            if (tanggalIjinTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("tanggalIjin"), tanggalIjinTo));
            }
            if (StringUtils.hasText(tipeIjin)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("tipeIjin")), tipeIjin.trim().toUpperCase()));
            }
            if (StringUtils.hasText(status)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")), status.trim().toUpperCase()));
            }
            if (StringUtils.hasText(namaSantri)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("namaSantri")),
                        "%" + namaSantri.trim().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<IjinResponseDto> data = ijinRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "updated", "id"))
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, data));
    }

    public ResponseEntity<BaseResponse<IjinResponseDto>> getById(Long id) {
        log.info("get ijin by id {}", id);
        Ijin ijin = findIjinById(id);
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, toResponse(ijin)));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<BaseResponse<IjinResponseDto>> create(IjinRequestDto request) {
        log.info("create ijin for {}", request.getNamaSantri());
        Ijin ijin = new Ijin();
        applyRequest(ijin, request);
        ijin.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        ijinRepository.save(ijin);
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.CREATE_SUCCESS, toResponse(ijin)));
    }

    public ResponseEntity<BaseResponse<IjinResponseDto>> approval(Long id, IjinApprovalRequestDto request) {
        log.info("appoval ijin for {}", id);
        Ijin ijin = ijinRepository.findById(id).orElseThrow(() -> new BadRequestException(ResponseCode.IJIN_NOT_FOUND));
        ijin.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        ijin.setStatus(request.getStatus());
        ijinRepository.save(ijin);
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse("APPROVED".equalsIgnoreCase(ijin.getStatus()) ? ResponseCode.IJIN_APPROVED : ResponseCode.IJIN_REJECTED, toResponse(ijin)));
    }

    private Ijin findIjinById(Long id) {
        return ijinRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ResponseCode.IJIN_NOT_FOUND));
    }

    private void applyRequest(Ijin ijin, IjinRequestDto request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        String role = user.getRole().getName();
        String status;
        if ("PENGURUS".equalsIgnoreCase(role)) {
            status = "APPROVED";
        } else {
            status = "PENDING";
        }
        ijin.setNamaSantri(request.getNamaSantri().trim());
        ijin.setTanggalIjin(request.getTanggalIjin());
        ijin.setTipeIjin(request.getTipeIjin().trim().toUpperCase());
        ijin.setAlasan(request.getAlasan().trim());
        ijin.setStatus(status);
    }

    private IjinResponseDto toResponse(Ijin ijin) {
        return IjinResponseDto.builder()
                .id(ijin.getId())
                .namaSantri(ijin.getNamaSantri())
                .tanggalIjin(ijin.getTanggalIjin())
                .tipeIjin(ijin.getTipeIjin())
                .alasan(ijin.getAlasan())
                .status(ijin.getStatus())
                .build();
    }
}

