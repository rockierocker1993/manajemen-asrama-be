package id.manajemenasrama.exception;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();
    private final HttpServletRequest request = new MockHttpServletRequest();

    @Test
    void handleBadRequestException_should_return_400_and_response_code() {
        ResponseEntity<BaseResponse> response = handler.handleBadRequestException(
                new BadRequestException(ResponseCode.ASRAMA_NOT_FOUND),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResponseCode()).isEqualTo(ResponseCode.ASRAMA_NOT_FOUND.getCode());
    }

    @Test
    void handleUnauthorizedException_should_return_401_and_error_code() {
        ResponseEntity<BaseResponse> response = handler.handleUnauthorizedException(
                new UnauthorizedException(ResponseCode.INVALID_USERNAME_PASSWORD),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResponseCode()).isEqualTo(ResponseCode.INVALID_USERNAME_PASSWORD.getCode());
    }

    @Test
    void handleGeneralException_should_return_500_and_unknown_error_code() {
        ResponseEntity<BaseResponse> response = handler.handleGeneralException(
                new RuntimeException("boom"),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResponseCode()).isEqualTo(ResponseCode.UKNOWN_ERROR.getCode());
    }
}

