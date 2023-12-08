package dev.shingi.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtils {

    private static final String[] COMMON_DATE_FORMATS = {
        "yyyy-MM-dd", "dd-MM-yyyy", "MM/dd/yyyy", "yyyy/MM/dd",
        "dd/MM/yyyy", "MM-dd-yyyy", "yyyyMMdd", "ddMMyyyy"
    };

    public static boolean isCellDateFormatted(Cell cell) {
        if (cell == null) return false;
        return org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell);
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String previewDateFromPattern(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    public static String detectDateFormat(Sheet sheet, int dateColumnIndex, int headerRowNumber) {
        List<String> sampleDates = new ArrayList<>();

        // Collect some sample dates from the column
        for (int i = headerRowNumber; i <= headerRowNumber + 10; i++) { // Assuming header is at row 0
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(dateColumnIndex);
                if (cell != null) {
                    sampleDates.add(cell.toString());
                }
            }
        }

        for (String format : COMMON_DATE_FORMATS) {
            if (isValidFormat(format, sampleDates)) {
                return format;
            }
        }

        return null; // Couldn't determine the date format
    }

    private static boolean isValidFormat(String format, List<String> dates) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        for (String date : dates) {
            try {
                simpleDateFormat.parse(date);
            } catch (ParseException e) {
                return false; // This format doesn't match
            }
        }
        return true; // All dates matched this format
    }

    public static String[] getCommonDateFormats() {
        return COMMON_DATE_FORMATS;
    }
}

