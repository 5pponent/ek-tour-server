package renewal.ektour.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindEstimateRequest {

    @NotBlank
    @Size(min = 11, max = 11, message = "01012341234 형식이 아닙니다")
    private String phone;

    @NotBlank
    @Size(min = 4, max = 4, message = "비밀번호는 4자리로 설정해주세요")
    @PositiveOrZero(message = "비밀번호는 0~9자리 숫자로만 가능합니다")
    private String password;
}
