package cn.joymates.infoin.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.io.Files;

/**
 * Excel file Reader
 * 
 * @author Jackie Hou
 * 
 */
public class XlsReader {
	private static Logger log = Logger.getLogger(XlsReader.class);
	
	private final static String SUFFIX1 = "xls";
	private final static String SUFFIX2 = "xlsx";
	
	private static String fileName;
	
	/**
	 * excel mapping properties
	 */
	public static Map<String, String> sheetNameMap = Maps.newHashMap();
	
	/**
	 * Read excel
	 * 
	 * @param filePath
	 */
	public static Map<String, Table<String, String, String>> readExcelFile(String filePath, String _fileName) {
		if (StringUtils.isEmpty(filePath)) {
			throw new RuntimeException("file not found！ filePath = " + filePath);
		}
		
		fileName = _fileName;
		
		Map<String, Table<String, String, String>> map = null;
		String suffix = Files.getFileExtension(_fileName);
		log.info("Ready for reading excel file, fileName=" + fileName);
		
		try {
			InputStream input = new FileInputStream(filePath);
			
			if (suffix.equals(SUFFIX1)) {
				map = readSheets(new HSSFWorkbook(input));
			} else {
				map = readSheets(new XSSFWorkbook(input));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;

	}
	
	//Get all Data inclue titles
	private static Map<String, Table<String, String, String>> readSheets(Workbook wb) {
		Map<String, Table<String, String, String>> allDatas = Maps.newHashMap();
		int sheetCount = wb.getNumberOfSheets();
		
		for (int i=0; i<sheetCount; i++) {
			String sheetName = wb.getSheetName(i);
			
			log.debug("Start to read sheet" + i + ", sheetName=" + sheetName);
			Table<String, String, String> datas = readDataInSheet(wb.getSheet(sheetName));
			allDatas.put(sheetName, datas);
		}
		
		return allDatas;
	}
	
	private static Table<String, String, String> readDataInSheet(Sheet sheet) {
		Table<String, String, String> sheetTable = HashBasedTable.create();
		Map<Integer, String> colNameMap = readTitle(sheet, sheetTable);
		readContent(sheet, colNameMap, sheetTable);
		
		log.info("Reading Complete!");
		return sheetTable;
	}
	
	private static Map<Integer, String> readTitle(Sheet sheet, Table<String, String, String> dataTable) {
		Map<Integer, String> colNameMap = Maps.newHashMap();
		
		Row r = sheet.getRow(0);
		if (r == null) {
			log.debug("no title in this sheet!");
			return colNameMap;
		}
		
		int columnCount = r.getPhysicalNumberOfCells(); 
		
		log.debug("----start read title");
		for (int j=0; j<columnCount; j++) {
			log.debug("--------read title " + (j + 1));
			Cell c = r.getCell(j);
			
			//title has empty cell, skip it!
			
			String v = "";
			if (c == null || StringUtils.isEmpty(c.toString())) {
				continue;
			} else {
//				colNameMap.put(j, CellReference.convertNumToColString(c.getColumnIndex()));
				colNameMap.put(j, (v = getNotStringValue(c)));
			}
			
			//get other type of data, except string type!
			dataTable.put(String.valueOf(1), colNameMap.get(j), "");
			
			//column index
//			dataTable.put(String.valueOf(0), "" + j, colNameMap.get(j));
		}
		
		log.debug("this sheet has " + colNameMap.size() + " cells");
		return colNameMap;
		
	}
	
	private static void readContent(Sheet sheet, Map<Integer, String> colNameMap, Table<String, String, String> dataTable) {
		int rowCount = sheet.getPhysicalNumberOfRows();
		int columnCount = colNameMap.size();
		
		for (int i=1; i<rowCount; i++) {
			log.debug("----read row " + (i + 1));
			Row r = sheet.getRow(i);
			
			//row empty flag!
			int emptyCount = 0;
			
			//read cells
			for (int j=0; j<columnCount; j++) {
				log.debug(("--------read cell " + (j + 1)));
				Cell c = r.getCell(j);
				
				if (c == null || StringUtils.isEmpty(c.toString())) {
					emptyCount ++;
				}
				
				dataTable.put(String.valueOf(i + 1), colNameMap.get(j), getNotStringValue(c));
			}
			
			//remove empty row
			if (emptyCount == columnCount) {
				
				log.debug("this row is a empty row! Remove it");
				for (int k=0; k<columnCount; k++) {
					dataTable.remove("" + (i + 1), colNameMap.get(k));
				}
				
			}
			
		}
	}
	private static String getNotStringValue(Cell c) {
		if (c == null) {
			return "";
		}
		
		String cstr = c.toString();
		
		//date type
		if (StringUtils.contains(cstr, "-")) {
			DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
			cstr = d.format(c.getDateCellValue());
			return cstr;
		}
		
		//numeric, but as a telephone number.
		if (Cell.CELL_TYPE_NUMERIC == c.getCellType() && c.getNumericCellValue() > Byte.MAX_VALUE) {
			NumberFormat nf = NumberFormat.getIntegerInstance();
			cstr = nf.format(c.getNumericCellValue());
			cstr = StringUtils.remove(cstr, ",");
			
		}
		return cstr;
	}
	

}
