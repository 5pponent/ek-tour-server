package renewal.ektour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static renewal.ektour.dto.response.ErrorResponse.convertJson;

@Getter
@AllArgsConstructor
@Slf4j
public class RestResponse<T> {

    public static <T> ResponseEntity<T> success(T body) {
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public static ResponseEntity<BoolResponse> success() {
        return ResponseEntity.status(HttpStatus.OK).body(new BoolResponse(true));
    }

    public static <T> ResponseEntity<T> notFound(T body) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    public static <T> ResponseEntity<T> badRequest(T body) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public static ResponseEntity<ErrorListResponse> badRequest(BindingResult bindingResult) {
        log.error("validation errors = {}", bindingResult.getFieldErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertJson(bindingResult.getFieldErrors()));
    }

}
