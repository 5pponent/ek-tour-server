package renewal.ektour.exception

import org.springframework.validation.BindingResult

class AdminException(message: String): RuntimeException(message)
class EmailException(message: String): RuntimeException(message)
class ExcelException(message: String): RuntimeException(message)

class ValidationException(
    var bindingResult: BindingResult
): RuntimeException("validation error")

// Error codes
const val VALIDATION_ERROR: String = "validation_error"