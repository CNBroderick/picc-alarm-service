package ape.alarm.common.email;

import dataq.core.data.schema.Field;
import dataq.core.data.schema.Record;
import dataq.core.data.schema.Recordset;
import dataq.core.data.schema.Schema;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.bklab.flow.util.number.DigitalFormatter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;

public class AlarmExcelAttachmentBuilder {

    private final XSSFWorkbook workbook;
    private final String title;

    public AlarmExcelAttachmentBuilder(String title) {
        this.title = title;
        this.workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
    }

    public AlarmExcelAttachmentBuilder createSheet(String sheetName, Recordset recordset) {
        return createSheet(sheetName, recordset.getSchema(), recordset.asList());
    }

    public AlarmExcelAttachmentBuilder createSheet(String sheetName, Schema schema, Collection<Record> records) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
//        XSSFRow titleRow = createTitleRow(sheet, this.title + "-" + sheetName + "表", schema.size());
        XSSFRow headerRow = createHeaderRow(sheet, schema, 0);
        XSSFRow bodyRow = createBodyRow(sheet, records, headerRow.getRowNum() + 1);
        return this;
    }

    private XSSFRow createTitleRow(XSSFSheet sheet, String title, int length) {
        XSSFRow row = sheet.createRow(0);

        XSSFCell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(title);

        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(28);
        style.setFont(font);
        cell.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(),
                cell.getColumnIndex(), cell.getColumnIndex() + length));

        row.setHeight((short) 30);

        return row;
    }

    private XSSFRow createHeaderRow(XSSFSheet sheet, Schema schema, int rowNumber) {
        XSSFRow row = sheet.createRow(rowNumber);
        Field[] fields = schema.fields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = Objects.toString(field.getCaption(), field.getName());
            XSSFCell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(name);
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            cell.setCellStyle(style);
        }
        return row;
    }

    private XSSFRow createBodyRow(XSSFSheet sheet, Collection<Record> records, int rowNumber) {
        int currentRowNumber = rowNumber;
        Map<Integer, Integer> widthMap = new LinkedHashMap<>();
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        for (Record record : records) {
            XSSFRow row = sheet.createRow(currentRowNumber);

            Field[] fields = record.getSchema().fields();
            for (int i = 0; i < fields.length; i++) {
                Object object = record.getObject(fields[i].getName());
                String serialize = serialize(object);
                if (serialize != null && serialize.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
                    serialize = serialize.substring(0, SpreadsheetVersion.EXCEL2007.getMaxTextLength());
                }
                XSSFCell cell = row.createCell(i, computeCellType(serialize));
                cell.setCellValue(serialize);
                cell.setCellStyle(style);
                widthMap.put(i, Math.max(calcWidth(serialize), widthMap.getOrDefault(i, 0)));
            }
            currentRowNumber++;
        }
        widthMap.forEach((index, width) -> sheet.setColumnWidth(index, Math.min(65280, Math.max(2843, width * 252 + 323))));
        return sheet.getRow(currentRowNumber - 1);
    }

    private CellType computeCellType(String object) {
        if (object == null) return CellType.BLANK;
        try {
            Double.parseDouble(object);
            return CellType.NUMERIC;
        } catch (Exception ignore) {
        }
        return CellType.STRING;
    }

    private int calcWidth(String content) {
        if (content == null || content.isBlank()) return 1;
        int count = 0;
        for (char c : content.toCharArray()) {
            count += c < 256 ? 1 : 2;
        }
        return count;
    }

    private String serialize(Object o) {
        if (o == null) {
            return "";
        } else {
            try {
                if (o instanceof String) {
                    return (String) o;
                }

                if (o instanceof Number) {
                    if (((Number) o).doubleValue() % 0.001 > 0) {
                        return new DigitalFormatter(((Number) o).doubleValue()).toInteger();
                    }

                    return new DigitalFormatter(((Number) o).doubleValue()).toFormatted();
                }

                if (o instanceof ChronoLocalDate) {
                    return DateTimeFormatter.ofPattern("uuuu-MM-dd").format((Temporal) o);
                }

                if (o instanceof ChronoLocalDateTime) {
                    return DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").format((Temporal) o);
                }

                if (o instanceof LocalTime) {
                    return DateTimeFormatter.ofPattern("HH:mm:ss").format((Temporal) o);
                }

                if (o instanceof Date) {
                    return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format((Date) o);
                }
            } catch (Exception e) {
                return "";
            }

            return String.valueOf(o);
        }
    }

    public ByteArrayOutputStream createDownloadStream() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        return stream;
    }
}
