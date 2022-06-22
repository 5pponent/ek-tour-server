package renewal.ektour.dto.request;

import lombok.Data;

@Data
public class AdminPasswordForm {

    private String password;
    private String passwordCheck;

    public boolean passwordCheck() {
        if (password == null && passwordCheck == null) return false;
        return this.password.equals(this.passwordCheck);
    }
}
