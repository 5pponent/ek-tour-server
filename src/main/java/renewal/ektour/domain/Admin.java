package renewal.ektour.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Admin {

    @Id @GeneratedValue
    @Column(name = "admin_id")
    private Long id;

    private String adminPassword;
}
