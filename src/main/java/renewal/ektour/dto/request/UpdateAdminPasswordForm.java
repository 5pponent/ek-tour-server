package renewal.ektour.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateAdminPasswordForm {

    @NotBlank
    private String nowPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordCheck;

    public boolean passwordCheck() {
        if (newPassword == null && newPasswordCheck == null) return false;
        return this.newPassword.equals(this.newPasswordCheck);
    }
}
