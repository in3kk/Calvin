package calvin.calvin.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"페이지를 찾을 수 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED,"로그인이 필요한 서비스 입니다."),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN,"권한이 없습니다.");

    private HttpStatus status;
    private String message;
}
