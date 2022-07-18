package renewal.ektour.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExcelServiceTest {

    @Autowired
    ExcelService excelService;

    @Test
    @DisplayName("엑셀 다운로드")
    void createExcel() {
        // given
        HttpServletResponse response = new MockHttpServletResponse();
        Long estimateId = 1L;

        // when

        // then
        assertThatNoException().isThrownBy(() -> {
            excelService.createExcel(estimateId, response);
        });
    }
}