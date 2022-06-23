package renewal.ektour.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminPasswordForm {

    @NotBlank
    private String password;

    @NotBlank
    private String passwordCheck;

    public boolean passwordCheck() {
        if (password == null && passwordCheck == null) return false;
        return this.password.equals(this.passwordCheck);
    }
}
