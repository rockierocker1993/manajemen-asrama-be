package id.manajemenasrama.exception;

import id.manajemenasrama.constant.ResponseCode;
import lombok.Getter;

@Getter
public class InternalServerErrorException extends RuntimeException {

    private final ResponseCode responseCode;

    public InternalServerErrorException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
