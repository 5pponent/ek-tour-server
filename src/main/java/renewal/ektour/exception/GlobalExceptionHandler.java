package renewal.ektour.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import renewal.ektour.dto.response.ErrorListResponse;

import static renewal.ektour.dto.response.RestResponse.badRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String errorHandle(Exception ex, Model model) {
        log.error("예외 발생 및 처리 = {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorListResponse> errorHandle(ValidationException ex) {
        log.error("검증 예외 발생 및 처리 = {}", ex.getMessage());
        return badRequest(ex.getBindingResult());
    }


}
