package id.manajemenasrama.exception;

import id.manajemenasrama.constant.ResponseCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private ResponseCode responseCode = ResponseCode.UNAUTHORIZED;

    public UnauthorizedException() {}

    public UnauthorizedException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
