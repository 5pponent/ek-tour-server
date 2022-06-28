package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import renewal.ektour.domain.Estimate;
import renewal.ektour.exception.ExcelException;
import renewal.ektour.repository.EstimateRepository;
import renewal.ektour.util.AdminConfig;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final EstimateRepository repository;

    // 특정 셀에 특정 값 넣기
    private void setValue(Sheet sheet, String position, String value) {
        CellReference ref = new CellReference(position);
        Row r = sheet.getRow(ref.getRow());
        if (r != null) {
            Cell c = r.getCell(ref.getCol());
            c.setCellValue(value);
        }
    }

    // 엑셀 파일 불러와서 값 수정
    public void createExcel(Long estimateId, HttpServletResponse response) {
        try {
            // 견적 가져오기
            Estimate estimate = repository.findById(estimateId).orElseThrow();

            // 엑셀 파일 불러오기
            OPCPackage opcPackage = OPCPackage.open(new File(AdminConfig.LINUX_EXCEL_PATH));
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
            String sheetName = workbook.getSheetName(0);
            Sheet sheet = workbook.getSheet(sheetName);

            // 데이터 세팅
            setValue(sheet, "C4", estimate.getName()); // 수신
            setValue(sheet, "C5", estimate.getEmail()); // 이메일
            setValue(sheet, "C6", convertPhone(estimate.getPhone())); // 연락처
            setValue(sheet, "C8", convertDateWithYear(estimate.getCreatedDate())); // 견적일
            setValue(sheet, "C9", convertDateWithYear(estimate.getValidDate())); // 유효일

            // 차량-내용
            String date = convertDate(estimate.getDepartDate()) + "~" + convertDate(estimate.getArrivalDate());
            setValue(sheet, "C14", date);
            String content = estimate.getArrivalPlace() + " ~ " + estimate.getArrivalPlace();
            setValue(sheet, "F14", content);
            setValue(sheet, "L14", estimate.getVehicleType().substring(0, 4)); // 규격
            setValue(sheet, "N14", Integer.toString(estimate.getVehicleNumber())); // 댓수
            setValue(sheet, "O14", "대");

            // 다운로드
            response.setContentType("ms-vnd/excel");
            String fileName = "견적서_" + estimate.getName() + "님_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            // 엑셀 다운로드시 한글 깨짐 처리
            String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");
            response.setHeader("Content-Disposition", "attachment;filename=" + outputFileName + ".xlsx");
            response.setStatus(200);
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            throw new ExcelException("엑셀 변환/다운로드 관련 오류 발생");
        }
    }

    // 06-29 형식
    private String convertDate(String date){
        return date.substring(5, 10);
    }

    // 2022-06-29 형식
    private String convertDateWithYear(String date) {
        return date.substring(0, 10);
    }

    // 010-1234-1234 형식
    private String convertPhone(String phone) {
        String top = phone.substring(0, 3);
        String mid = phone.substring(3, 7);
        String bottom = phone.substring(7, 11);
        return top + "-" + mid + "-" + bottom;
    }
}
