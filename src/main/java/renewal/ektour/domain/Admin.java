package renewal.ektour.domain;

import lombok.*;
import renewal.ektour.dto.response.CompanyInfoResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    private String kakaoTalkId;

    public Admin(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Builder
    public Admin(String adminPassword, String adminName, String infoHandlerName, String businessNum, String registrationNum, String address, String tel, String fax, String phone, String email, String accountBank, String accountNum, String accountHolder, String kakaoTalkId) {
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
        this.kakaoTalkId = kakaoTalkId;
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
                .kakaoTalkId(kakaoTalkId)
                .build();
    }

    public void updateCompanyInfo(CompanyInfoResponse info) {
        this.adminName = info.getAdminName();
        this.infoHandlerName = info.getInfoHandlerName();
        this.businessNum = info.getBusinessNum();
        this.registrationNum = info.getRegistrationNum();
        this.address = info.getAddress();
        this.tel = info.getTel();
        this.fax = info.getFax();
        this.phone = info.getPhone();
        this.email = info.getEmail();
        this.accountBank = info.getAccountBank();
        this.accountNum = info.getAccountNum();
        this.accountHolder = info.getAccountHolder();
        this.kakaoTalkId = info.getKakaoTalkId();
    }

    public void updatePassword(String newPassword) {
        this.adminPassword = newPassword;
    }
}
