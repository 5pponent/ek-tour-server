package renewal.ektour.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id @GeneratedValue
    @Column(name = "admin_id")
    private Long id;

    private String adminPassword;

    public Admin(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public void updatePassword(String newPassword) {
        this.adminPassword = newPassword;
    }
}
