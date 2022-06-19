package renewal.ektour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.FieldError;
import renewal.ektour.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;

    public static ErrorResponse convertJson(FieldError error) {
        return new ErrorResponse(ErrorCode.VALIDATION_ERROR, error.getField() + " / " + error.getDefaultMessage());
    }

    public static ErrorListResponse convertJson(List<FieldError> bindingResults) {
        ArrayList<ErrorResponse> result = new ArrayList<>();
        for (FieldError e : bindingResults) {
            result.add(ErrorResponse.convertJson(e));
        }
        return new ErrorListResponse(result);
    }
}

