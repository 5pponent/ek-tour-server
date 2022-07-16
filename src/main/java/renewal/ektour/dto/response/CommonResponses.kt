package renewal.ektour.dto.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import renewal.ektour.dto.response.ErrorResponse.Companion.convertJson
//import renewal.ektour.exception.ErrorCode
import renewal.ektour.exception.VALIDATION_ERROR

data class BoolResponse(
    val result: Boolean = true,
)

data class ErrorResponse(
    val code: String = "",
    val message: String = "",
) {
    companion object {
        @JvmStatic fun convertJson(error: FieldError): ErrorResponse {
            return ErrorResponse(VALIDATION_ERROR, error.field + " / " + error.defaultMessage)
        }

        @JvmStatic fun convertJson(bindingResults: List<FieldError?>): ErrorListResponse {
            val result = ArrayList<ErrorResponse>()
            for (e in bindingResults) {
                e?.let { this.convertJson(it) }?.let { result.add(it) }
            }
            return ErrorListResponse(result)
        }
    }

}

data class ErrorListResponse(
    val errors: List<ErrorResponse>,
)

class RestResponse {
    companion object {
        @JvmStatic fun <T> success(body: T): ResponseEntity<T>? {
            return ResponseEntity.status(HttpStatus.OK).body(body)
        }

        @JvmStatic fun <T> success(): ResponseEntity<BoolResponse> {
            return ResponseEntity.status(HttpStatus.OK).body(BoolResponse(true))
        }

        @JvmStatic fun <T> badRequest(body: T): ResponseEntity<T>? {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
        }

        @JvmStatic fun badRequest(bindingResult: BindingResult): ResponseEntity<ErrorListResponse>? {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertJson(bindingResult.fieldErrors))
        }
    }
}