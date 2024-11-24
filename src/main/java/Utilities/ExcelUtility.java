package Utilities;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtility {

    private DataFormatter dataFormatter;
    private Sheet sheet;
    private Workbook workbook;
    private FileInputStream fis;

    public ExcelUtility(String filePath, String sheetName) throws IOException {
        this.dataFormatter = new DataFormatter();
        this.fis = new FileInputStream(filePath);
        this.workbook = WorkbookFactory.create(fis);
        this.sheet = workbook.getSheet(sheetName);
    }

    // Method to fetch all data from the Excel sheet
    public Object[][] getExcelData() {
        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getLastCellNum();  // Assuming all rows have the same number of columns
        Object[][] data = new Object[rowCount - 1][colCount];

        // Iterating through each row and column to fetch data
        for (int i = 1; i < rowCount; i++) {  // Skipping header row
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = dataFormatter.formatCellValue(row.getCell(j));
            }
        }

        return data;
    }

    // Close the file stream and workbook
    public void close() throws IOException {
        if (fis != null) {
            fis.close();
        }
        if (workbook != null) {
            workbook.close();
        }
    }
}
