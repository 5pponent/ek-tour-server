package renewal.ektour.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Service
public class ExcelService {

    public static final String FILE_PATH = "C:\\dev\\estimate.xlsx";
    private final String RECEIVER = "C4";
    private final String EMAIL = "C5";
    private final String PHONE = "C6";
    private final String COMPANY = "C7";
    private final String ESTIMATE_DATE = "C8";
    private final String VALID_DATE = "C9";

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
    public void writeExcel(HttpServletResponse response) throws InvalidFormatException, IOException {
        OPCPackage opcPackage = OPCPackage.open(new File(FILE_PATH));
        XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
        String sheetName = workbook.getSheetName(0);
        System.out.println(sheetName);
        Sheet sheet = workbook.getSheet(sheetName);
        setValue(sheet, RECEIVER, "테스트");
        setValue(sheet, EMAIL, "테스트2");
        setValue(sheet, PHONE, "테스트3");
        setValue(sheet, COMPANY, "테스트4");
        setValue(sheet, ESTIMATE_DATE, "테스트5");
        setValue(sheet, VALID_DATE, "테스트50");
        download(workbook, response);
    }

    // response 객체를 통해서 엑셀 파일 다운로드
    public void download(Workbook workbook, HttpServletResponse response) throws IOException {
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=estimate.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
