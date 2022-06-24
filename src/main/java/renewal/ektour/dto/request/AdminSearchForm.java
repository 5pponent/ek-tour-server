package renewal.ektour.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class AdminSearchForm {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    private String searchType;
    private String keyword;
}
