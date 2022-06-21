package renewal.ektour.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class FindEstimateRequest {

    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "010-1234-1234 또는 01012341234 형식이 아닙니다")
    private String phone;

    @NotBlank
    @Size(min = 4, max = 4, message = "비밀번호는 4자리로 설정해주세요")
    @PositiveOrZero(message = "비밀번호는 0~9자리 숫자로만 가능합니다")
    private String password;
}
