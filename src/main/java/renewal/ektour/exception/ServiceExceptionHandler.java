package renewal.ektour.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String errorHandle(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }


}
