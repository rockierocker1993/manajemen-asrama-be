package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.asrama.AsramaRequestDto;
import id.manajemenasrama.dto.asrama.AsramaResponseDto;
import id.manajemenasrama.entity.Asrama;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.repository.AsramaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsramaServiceTest {

    @Mock
    private AsramaRepository asramaRepository;

    @InjectMocks
    private AsramaService asramaService;

    @Test
    void create_should_save_trimmed_name() {
        when(asramaRepository.findByNamaIgnoreCase("Asrama Putra")).thenReturn(Optional.empty());
        when(asramaRepository.save(any(Asrama.class))).thenAnswer(invocation -> {
            Asrama asrama = invocation.getArgument(0);
            asrama.setId(1L);
            return asrama;
        });

        ResponseEntity<?> response = asramaService.create(AsramaRequestDto.builder()
                .nama(" Asrama Putra ")
                .build());

        ArgumentCaptor<Asrama> captor = ArgumentCaptor.forClass(Asrama.class);
        verify(asramaRepository).save(captor.capture());
        assertThat(captor.getValue().getNama()).isEqualTo("Asrama Putra");
        BaseResponse<?> body = (BaseResponse<?>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getResponseCode()).isEqualTo(ResponseCode.CREATE_SUCCESS.getCode());
        assertThat(body.getData()).isInstanceOf(AsramaResponseDto.class);
    }

    @Test
    void create_should_reject_duplicate_name() {
        when(asramaRepository.findByNamaIgnoreCase("Asrama Putra")).thenReturn(Optional.of(Asrama.builder().nama("Asrama Putra").build()));

        assertThatThrownBy(() -> asramaService.create(AsramaRequestDto.builder()
                .nama(" Asrama Putra ")
                .build()))
                .isInstanceOf(BadRequestException.class)
                .extracting(ex -> ((BadRequestException) ex).getResponseCode())
                .isEqualTo(ResponseCode.ASRAMA_ALREADY_EXIST);
    }
}

