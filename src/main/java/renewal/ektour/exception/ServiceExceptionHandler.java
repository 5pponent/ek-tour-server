package renewal.ektour.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import renewal.ektour.dto.response.ErrorListResponse;

import static renewal.ektour.dto.response.RestResponse.badRequest;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String errorHandle(Exception ex, Model model) {
        log.error("예외 발생 및 처리 = {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorListResponse> errorHandle(ValidationException ex) {
        log.error("예외 발생 및 처리 = {}", ex.getMessage());
        return badRequest(ex.getBindingResult());
    }


}
