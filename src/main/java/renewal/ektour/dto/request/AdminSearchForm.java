package renewal.ektour.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AdminSearchForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    private String searchType;
    private String keyword;
}
