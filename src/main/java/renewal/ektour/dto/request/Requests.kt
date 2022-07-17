package renewal.ektour.dto.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

data class AdminSearchForm(
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        var start: LocalDate? = null,

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        var end: LocalDate? = null,

        var searchType: String = "",
        var keyword: String = "",
)

data class FindEstimateRequest(

        @NotBlank
        @Size(min = 11, max = 11, message = "01012341234 형식이 아닙니다")
        var phone: String = "",

        @NotBlank
        @Size(min = 4, max = 4, message = "비밀번호는 4자리로 설정해주세요")
        @PositiveOrZero(message = "비밀번호는 0~9자리 숫자로만 가능합니다")
        var password: String = "",
)

data class UpdateAdminPasswordForm(
        @NotBlank var nowPassword: String = "",
        @NotBlank var newPassword: String = "",
        @NotBlank var newPasswordCheck: String = "",
) {
        fun passwordCheck(): Boolean {
                return this.newPassword == this.newPasswordCheck
        }
}