package renewal.ektour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
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

}
