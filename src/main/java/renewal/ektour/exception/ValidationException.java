//package renewal.ektour.exception;
//
//import lombok.Getter;
//import org.springframework.validation.BindingResult;
//
//@Getter
//public class ValidationException extends RuntimeException{
//    private final BindingResult bindingResult;
//
//    public ValidationException(String message, BindingResult bindingResult) {
//        super(message);
//        this.bindingResult = bindingResult;
//    }
//
//    public ValidationException(BindingResult bindingResult) {
//        super("validation error");
//        this.bindingResult = bindingResult;
//    }
//}
