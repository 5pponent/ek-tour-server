package renewal.ektour.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import renewal.ektour.dto.response.CompanyInfoResponse;

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
    private String adminName;
    private String infoHandlerName;
    private String businessNum;
    private String registrationNum;
    private String address;
    private String tel;
    private String fax;
    private String phone;
    private String email;
    private String accountBank;
    private String accountNum;
    private String accountHolder;

    public Admin(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Builder
    public Admin(String adminPassword, String adminName, String infoHandlerName, String businessNum, String registrationNum, String address, String tel, String fax, String phone, String email, String accountBank, String accountNum, String accountHolder) {
        this.adminPassword = adminPassword;
        this.adminName = adminName;
        this.infoHandlerName = infoHandlerName;
        this.businessNum = businessNum;
        this.registrationNum = registrationNum;
        this.address = address;
        this.tel = tel;
        this.fax = fax;
        this.phone = phone;
        this.email = email;
        this.accountBank = accountBank;
        this.accountNum = accountNum;
        this.accountHolder = accountHolder;
    }

    public CompanyInfoResponse toCompanyInfoResponse() {
        return CompanyInfoResponse.builder()
                .adminName(adminName)
                .infoHandlerName(infoHandlerName)
                .businessNum(businessNum)
                .registrationNum(registrationNum)
                .address(address)
                .tel(tel)
                .fax(fax)
                .phone(phone)
                .email(email)
                .accountBank(accountBank)
                .accountNum(accountNum)
                .accountHolder(accountHolder)
                .build();
    }

    public void updatePassword(String newPassword) {
        this.adminPassword = newPassword;
    }
}
