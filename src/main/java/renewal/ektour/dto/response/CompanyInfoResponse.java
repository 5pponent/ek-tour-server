package renewal.ektour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CompanyInfoResponse {

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
}
