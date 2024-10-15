package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelParser {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\linjo\\Downloads\\Q3.xlsx";  // 替換文件路徑
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            int planStartColumn = 5;  // 假設 Plan 名稱從第 F 列開始（索引 5）

            System.out.println("[Benefit, Coverage, Category, Plan Name, Coverage Value]");
            System.out.println("=========================================================");

            String currentMainBenefit = "";
            String currentSubBenefit = "";
            String lastNonEmptyA = "";
            String lastNonEmptyB = "";

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null || row.getZeroHeight()) continue;

                String valueA = getCellValueAsString(row.getCell(0));
                String valueB = getCellValueAsString(row.getCell(1));
                String valueC = getCellValueAsString(row.getCell(2));

                // 更新非空的 A 和 B 值
                if (!valueA.isEmpty()) lastNonEmptyA = valueA;
                if (!valueB.isEmpty()) lastNonEmptyB = valueB;

                // 判斷主要福利
                if (!valueA.isEmpty() && valueB.isEmpty() && valueC.isEmpty()) {
                    currentMainBenefit = valueA;
                    currentSubBenefit = "";
                    continue;  // 跳過主要福利的輸出
                }

                // 設置當前子福利和類別
                currentSubBenefit = !valueA.isEmpty() ? valueA : (!valueB.isEmpty() ? lastNonEmptyA : currentSubBenefit);
                String currentCategory = valueB;

                if (!currentCategory.isEmpty()) {
                    for (int colIndex = planStartColumn; colIndex < headerRow.getLastCellNum(); colIndex++) {
                        if (sheet.isColumnHidden(colIndex)) continue;

                        Cell planCell = row.getCell(colIndex);
                        Cell headerCell = headerRow.getCell(colIndex);

                        if (headerCell != null) {
                            String planName = headerCell.getStringCellValue();
                            String coverageValue = formatAsPercentage(getCellValueAsString(planCell));

                            System.out.printf("%s, %s, %s, %s, %s%n",
                                    currentMainBenefit, currentSubBenefit, currentCategory, planName, coverageValue);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatAsPercentage(String value) {
        try {
            double numericValue = Double.parseDouble(value);
            if (numericValue <= 1) {
                numericValue *= 100;
            }
            return String.format("%d%%", Math.round(numericValue));
        } catch (NumberFormatException e) {
            return value; // 如果無法解析為數字，返回原始值
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}