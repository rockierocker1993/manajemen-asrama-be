package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.ijin.IjinApprovalRequestDto;
import id.manajemenasrama.dto.ijin.IjinRequestDto;
import id.manajemenasrama.dto.ijin.IjinResponseDto;
import id.manajemenasrama.entity.Ijin;
import id.manajemenasrama.entity.Role;
import id.manajemenasrama.entity.User;
import id.manajemenasrama.repository.IjinRepository;
import id.manajemenasrama.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IjinServiceTest {

    @Mock
    private IjinRepository ijinRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IjinService ijinService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void create_should_auto_approve_for_pengurus() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("pengurus1", "secret")
        );
        Role role = Role.builder().name("PENGURUS").build();
        User user = User.builder().username("pengurus1").role(role).build();
        when(userRepository.findByUsername("pengurus1")).thenReturn(Optional.of(user));
        when(ijinRepository.save(any(Ijin.class))).thenAnswer(invocation -> {
            Ijin ijin = invocation.getArgument(0);
            ijin.setId(10L);
            return ijin;
        });

        ResponseEntity<?> response = ijinService.create(IjinRequestDto.builder()
                .namaSantri("Ahmad")
                .tanggalIjin(LocalDate.of(2026, 10, 16))
                .tipeIjin("ijin")
                .alasan("Pulang kampung")
                .build());

        ArgumentCaptor<Ijin> captor = ArgumentCaptor.forClass(Ijin.class);
        verify(ijinRepository).save(captor.capture());
        Ijin saved = captor.getValue();

        assertThat(saved.getNamaSantri()).isEqualTo("Ahmad");
        assertThat(saved.getTipeIjin()).isEqualTo("IJIN");
        assertThat(saved.getStatus()).isEqualTo("APPROVED");
        assertThat(saved.getCreatedBy()).isEqualTo("pengurus1");
        BaseResponse<?> body = (BaseResponse<?>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getResponseCode()).isEqualTo(ResponseCode.CREATE_SUCCESS.getCode());
    }

    @Test
    void create_should_set_pending_for_non_pengurus_role() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("ustadzah1", "secret")
        );
        Role role = Role.builder().name("USTADZAH").build();
        User user = User.builder().username("ustadzah1").role(role).build();
        when(userRepository.findByUsername("ustadzah1")).thenReturn(Optional.of(user));
        when(ijinRepository.save(any(Ijin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = ijinService.create(IjinRequestDto.builder()
                .namaSantri("Ali")
                .tanggalIjin(LocalDate.of(2026, 10, 16))
                .tipeIjin("SAKIT")
                .alasan("Sakit demam")
                .build());

        ArgumentCaptor<Ijin> captor = ArgumentCaptor.forClass(Ijin.class);
        verify(ijinRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("PENDING");
        assertThat(((BaseResponse<?>) response.getBody()).getResponseCode()).isEqualTo(ResponseCode.CREATE_SUCCESS.getCode());
    }

    @Test
    void approval_should_return_approved_response_code() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("pengurus1", "secret")
        );
        Ijin ijin = new Ijin();
        ijin.setId(1L);
        ijin.setStatus("PENDING");
        when(ijinRepository.findById(1L)).thenReturn(Optional.of(ijin));
        when(ijinRepository.save(any(Ijin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = ijinService.approval(1L, buildApprovalRequest("APPROVED"));

        assertThat(((BaseResponse<?>) response.getBody()).getResponseCode()).isEqualTo(ResponseCode.IJIN_APPROVED.getCode());
        assertThat(ijin.getStatus()).isEqualTo("APPROVED");
        assertThat(ijin.getUpdatedBy()).isEqualTo("pengurus1");
    }

    @Test
    void approval_should_return_rejected_response_code() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("pengurus1", "secret")
        );
        Ijin ijin = new Ijin();
        ijin.setId(1L);
        ijin.setStatus("PENDING");
        when(ijinRepository.findById(1L)).thenReturn(Optional.of(ijin));
        when(ijinRepository.save(any(Ijin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = ijinService.approval(1L, buildApprovalRequest("REJECTED"));

        assertThat(((BaseResponse<?>) response.getBody()).getResponseCode()).isEqualTo(ResponseCode.IJIN_REJECTED.getCode());
        assertThat(ijin.getStatus()).isEqualTo("REJECTED");
    }

    @Test
    void list_should_return_mapped_data_from_repository() {
        Ijin ijin = new Ijin();
        ijin.setId(1L);
        ijin.setNamaSantri("Ahmad");
        ijin.setTanggalIjin(LocalDate.of(2026, 10, 16));
        ijin.setTipeIjin("IJIN");
        ijin.setAlasan("Pulang");
        ijin.setStatus("PENDING");
        when(ijinRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Ijin>>any(), any(Sort.class))).thenReturn(List.of(ijin));

        ResponseEntity<?> response = ijinService.list(null, null, null, null, null);

        verify(ijinRepository).findAll(org.mockito.ArgumentMatchers.<Specification<Ijin>>any(), any(Sort.class));
        BaseResponse<?> body = (BaseResponse<?>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getResponseCode()).isEqualTo(ResponseCode.SUCCESS.getCode());
        assertThat(body.getData()).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        List<IjinResponseDto> data = (List<IjinResponseDto>) body.getData();
        assertThat(data).hasSize(1);
        assertThat(data.get(0).getNamaSantri()).isEqualTo("Ahmad");
    }

    private IjinApprovalRequestDto buildApprovalRequest(String status) {
        IjinApprovalRequestDto request = new IjinApprovalRequestDto();
        request.setStatus(status);
        return request;
    }
}

