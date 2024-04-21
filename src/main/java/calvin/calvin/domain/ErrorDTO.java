package calvin.calvin.domain;

import calvin.calvin.error.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorDTO {
    private String result;
    private ErrorCode errorCode;
    private String message;

    public ErrorDTO(ErrorCode errorCode) {
        this.result = "ERROR";
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public ErrorDTO(ErrorCode errorCode, String message) {
        this.result = "ERROR";
        this.errorCode = errorCode;
        this.message = message;
    }
}
