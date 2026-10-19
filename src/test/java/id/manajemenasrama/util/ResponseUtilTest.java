package id.manajemenasrama.util;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResponseUtilTest {

    @Test
    void buildSuccessResponse_should_copy_values_from_response_code() {
        BaseResponse<String> response = ResponseUtil.buildSuccessResponse(ResponseCode.CREATE_SUCCESS, "payload");

        assertThat(response.getResponseCode()).isEqualTo(ResponseCode.CREATE_SUCCESS.getCode());
        assertThat(response.isStatus()).isTrue();
        assertThat(response.getMessageId()).isEqualTo(ResponseCode.CREATE_SUCCESS.getMessageId());
        assertThat(response.getMessageEn()).isEqualTo(ResponseCode.CREATE_SUCCESS.getMessageEn());
        assertThat(response.getTitleId()).isEqualTo(ResponseCode.CREATE_SUCCESS.getTitleId());
        assertThat(response.getTitleEn()).isEqualTo(ResponseCode.CREATE_SUCCESS.getTitleEn());
        assertThat(response.getData()).isEqualTo("payload");
    }

    @Test
    void buildSuccessResponse_should_reject_null_response_code() {
        assertThatThrownBy(() -> ResponseUtil.buildSuccessResponse(null, "payload"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("responseCode must not be null");
    }
}

