package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import renewal.ektour.service.ExcelService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final ExcelService excelService;

    @GetMapping("/test")
    public void test(HttpServletResponse response) throws IOException, InvalidFormatException {
        excelService.writeExcel(response);
    }
}
