package id.manajemenasrama.dto.ijin;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class IjinApprovalRequestDto {
    @Pattern(regexp = "(?i)APPROVED|REJECTED", message = "Status harus APPROVED atau REJECTED")
    private String status;
}
