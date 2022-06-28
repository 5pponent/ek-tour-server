package renewal.ektour.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String errorHandle(Exception ex, Model model) {
        log.error("예외 발생 및 처리 = {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }


}
