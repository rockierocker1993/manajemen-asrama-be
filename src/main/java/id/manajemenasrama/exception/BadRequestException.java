package id.manajemenasrama.exception;

import id.manajemenasrama.constant.ResponseCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ResponseCode responseCode;

    public BadRequestException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
