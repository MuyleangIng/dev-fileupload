package co.cstad.sen.api.excel;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@RestController
public class ExcelExportController {
    @GetMapping("/preview")
    public List<Map<String, Object>> previewData() {
        return fetchData(); // Reuse fetchData method to fetch the data
    }

    private List<Map<String, Object>> fetchData() {
        // This method should fetch your data and return it as List<Map<String, Object>>
        // For the sake of example, it's left with static data
        return List.of(
                Map.of("Name", "John Doe", "Age", 30),
                Map.of("Name", "Jane Doe", "Age", 25)
        );
    }
    @GetMapping("/view-excel")
    public void viewExcelInGoogleDocs(HttpServletResponse response) throws IOException {
        // Assume this method generates an Excel file, uploads it, and gets a public URL
        String publicFileUrl = uploadExcelAndGetPublicUrl();

        // Construct the Google Docs viewer URL with the Excel file's public URL
        String googleDocsUrl = "https://docs.google.com/gview?url=" + publicFileUrl + "&embedded=true";

        // Redirect the user to the Google Docs viewer URL
        response.sendRedirect(googleDocsUrl);
    }
    private String uploadExcelAndGetPublicUrl() {
        // Your logic to generate an Excel file, upload it, and return the public URL
        // This is a placeholder. You'll need to implement uploading to a cloud service or a similar approach.
        return "https://example.com/path/to/your/uploaded/excel/file.xlsx";
    }
    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=ASWDRGTY.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        List<Map<String, Object>> data = fetchData();

        Row headerRow = sheet.createRow(0);
        if (!data.isEmpty()) {
            int cellNum = 0;
            for (String key : data.get(0).keySet()) {
                headerRow.createCell(cellNum++).setCellValue(key);
            }
        }

        // Filling data
        int rowNum = 1;
        for (Map<String, Object> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            for (Object value : rowData.values()) {
                Cell cell = row.createCell(cellNum++);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                }
                // Handle other types as needed
            }
        }

        workbook.write(response.getOutputStream()); // Write workbook to response
        workbook.close();
    }

//    private List<Map<String, Object>> fetchData() {
//        // This method should fetch your data and return it as List<Map<String, Object>>
//        // For the sake of example, it's left empty
//        return List.of(
//                Map.of("Name", "John Doe", "Age", 30),
//                Map.of("Name", "Jane Doe", "Age", 25)
//        );
//    }
}
