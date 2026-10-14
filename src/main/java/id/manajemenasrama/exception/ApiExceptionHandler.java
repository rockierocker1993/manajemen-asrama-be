package id.manajemenasrama.exception;


import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse> handleBadRequestException(
            BadRequestException ex,
            HttpServletRequest request) {
        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity.badRequest().body(ResponseUtil.buildSuccessResponse(responseCode, null));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<BaseResponse> handleGeneralException(
            InternalServerErrorException ex,
            HttpServletRequest request) {

        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.buildSuccessResponse(responseCode, null));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseResponse> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request) {
        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.buildSuccessResponse(responseCode, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred: ", ex);
        ResponseCode responseCode = ResponseCode.UKNOWN_ERROR;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.buildSuccessResponse(responseCode, null));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<BaseResponse> handleAuthorizationDeniedException(Exception ex, HttpServletRequest request) {
        log.error("AuthorizationDeniedException ", ex);
        ResponseCode responseCode = ResponseCode.UNAUTHORIZED;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.buildSuccessResponse(responseCode, null));
    }
}
