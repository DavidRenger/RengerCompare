package dev.shingi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class FileUtils {
    static Alert wrongFileExtensionAlert = new Alert(AlertType.ERROR,
			"Verkeerd bestandstype, upload aub. alleen bestanden met de extensie .csv of .xlsx.", ButtonType.OK);

	public final static List<String> LEGAL_FILE_EXTENSIONS = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("csv");
			add("xlsx");
		}
	};

	// First, check if the file extension is legal. This refers to the list above. The method is called when creating a new TransactionsFile.
	public static boolean isLegalFileExtension(String fileExtension) {
		if (LEGAL_FILE_EXTENSIONS.contains(fileExtension)) {
			return true;
		} else {
			wrongFileExtensionAlert.showAndWait();
			return false;
		}
	}

	// Second, if the file extension is legal but is not xlsx already, convert it to xlsx. The relevant methods are below this one.
	public static XSSFSheet getXSSFSheetFromLegalFileTypes(File file, String fileExtension) throws IOException, CsvException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			switch (fileExtension) {
			case "csv":
				convertCsvToXlsx(file, workbook);
				break;
			case "xlsx":
				workbook = new XSSFWorkbook(new FileInputStream(file));
			}
			return workbook.getSheetAt(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// This method converts csv files to xlsx format.
	public static void convertCsvToXlsx(File file, XSSFWorkbook workbook) throws IOException, CsvException {
		// Read the CSV file
		try {
			CSVReader csvReader = new CSVReader(new FileReader(file));
			Sheet sheet = workbook.createSheet("transacties");
			List<String[]> records = csvReader.readAll();

			int rowNum = 0;
			for (String[] record : records) {
				Row row = sheet.createRow(rowNum++);
				int colNum = 0;
				for (String field : record) {
					Cell cell = row.createCell(colNum++);
					cell.setCellValue(field);
				}
			}
//	        return workbook;
		} catch (Exception e) {
			e.printStackTrace();
//			return null;
		}
	}
}