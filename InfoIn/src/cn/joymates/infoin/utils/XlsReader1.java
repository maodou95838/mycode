package cn.joymates.infoin.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.joymates.infoin.domain.School;
import cn.joymates.infoin.domain.Teacher;
import cn.joymates.infoin.domain.User;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.io.Files;

/**
 * Excel file Reader
 * 
 * @author Jackie Hou
 * 
 */

@Deprecated
public class XlsReader1 {
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
	public static Map<String, Object> readExcelFile(String filePath, String _fileName) {
		if (StringUtils.isEmpty(filePath)) {
			throw new RuntimeException("file not found！ filePath = " + filePath);
		}
		
		fileName = _fileName;
		
		Map<String, Object> map = null;
		String suffix = Files.getFileExtension(_fileName);
		try {
			InputStream input = new FileInputStream(filePath);
			
			if (suffix.equals(SUFFIX1)) {
				map = read(new HSSFWorkbook(input));
			} else {
				map = read(new XSSFWorkbook(input));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;

	}
	
	private static Map<String, Object> read(Workbook wb) {
		Map<String, Object> resultMap = Maps.newHashMap();
		
		int sheetCount = wb.getNumberOfSheets();
		List<String> allSheets = Lists.newArrayList();
		
		for (int i=0; i<sheetCount; i++) {
			allSheets.add(wb.getSheetAt(i).getSheetName());
		}
		
		VerifyUtils verify = new VerifyUtils();
		
		resultMap.put("school", readSchoolInfo(wb, allSheets, verify));
		resultMap.put("teacher", readTeacherInfo(wb, allSheets, verify));
		resultMap.put("user", readUserInfo(wb, allSheets, verify));
		
		return resultMap;
	}
	
	private static String readTitle(Sheet sheet, int columIndex) {
		Row row = sheet.getRow(0);
		return String.valueOf(row.getCell(columIndex));
	}
	
	private static List<String> readDatas(VerifyUtils verify,
			Sheet tSheet, Row row) {
		List<String> values = Lists.newArrayList();
		int cellCount = row.getPhysicalNumberOfCells();
		
		for (int j=0; j<cellCount; j++) {
			Cell c = row.getCell(j);
			String v = (c != null ? c.toString() : "");
//			verify.isEmpty(tSheet.getSheetName(), readTitle(tSheet, j), v);
			
			values.add(v);
		}
		
		return values;
	}
	
	private static School readSchoolInfo(Workbook wb, List<String> sheetNames, VerifyUtils verify) {
//		School school = readShoolSheet(wb, sheetNames, verify); 
//		
//		//
//		readGradeSheet(wb, sheetNames, verify, school);
//		
//		//
//		readTermSheet(wb, sheetNames, verify, school);
//		
//		//
//		readClassesSheet(wb, sheetNames, verify, school);
		
		
		Map<String, Table<String, String, String>> allData = readTeacherInfo1(wb);
		for (Map.Entry<String, Table<String, String, String>> entry : allData.entrySet()) {
			String key = entry.getKey();
			Table<String, String, String> table = entry.getValue();
			
			int rowsCount = table.rowMap().size();
			int columnCount = table.columnMap().size();
			Map<String, String> titles = Maps.newHashMap();
			
			for (int i=0; i<rowsCount; i++) {
				for (int j=0; j<columnCount; j++) {
					String v = table.get(String.valueOf(i), String.valueOf(j));
					int phoneIdx = -1;
					
					if (i == 0) {
						titles.put(String.valueOf(j), v);
						if (("电话").equals(v)) {
							phoneIdx = j;
						}
					} 
					
					if (i != 0) {
//						verify.isEmpty(key, i, titles.get("" + j), v);
						
						if (j == phoneIdx) {
//							verify.isCellPhoneExist(key, v);
						}
					}
					
				}
			}
		}
		
		return null;
	}

	private static void readClassesSheet(Workbook wb, List<String> sheetNames,
			VerifyUtils verify, School school) {
		String sheetName = sheetNameMap.get("classes");;
		Sheet claSheet = wb.getSheet(sheetName);
		
		int claRows = claSheet.getPhysicalNumberOfRows();
		ListMultimap<String, String> classes = ArrayListMultimap.create();
		
		for (int i=1; i<claRows; i++) {
			Row claR = claSheet.getRow(i);
			Cell c0 = claR.getCell(0);
			Cell c1 = claR.getCell(1);
			
			String v0 = (c0 != null ? c0.toString() : "");
			String v1 = (c1 != null ? c1.toString() : "");
//			
//			verify.isEmpty(sheetName, readTitle(claSheet, 0), v0);
//			verify.isEmpty(sheetName, readTitle(claSheet, 1), v1);
			
			classes.put(v0, v1);
		}
		school.setClasses(classes);
		sheetNames.remove(sheetName);
	}

	private static void readTermSheet(Workbook wb, List<String> sheetNames,
			VerifyUtils verify, School school) {
		String sheetName =sheetNameMap.get("term");;
		Sheet termSheet = wb.getSheet(sheetName);
		
		Row tRow = termSheet.getRow(1);
		int cellCount = tRow.getPhysicalNumberOfCells();
		String[] termInfo = new String[cellCount];
		
		for (int i=0; i<cellCount; i++) {
			Cell tCell = tRow.getCell(i);
			String v = (tCell != null ? tCell.toString() : "");
			
//			verify.isEmpty(sheetName, readTitle(termSheet, i), v);
			termInfo[i] = v;
		}
		school.setTermInfo(termInfo);
		sheetNames.remove(sheetName);
	}

	private static void readGradeSheet(Workbook wb, List<String> sheetNames,
			VerifyUtils verify, School school) {
		String sheetName = sheetNameMap.get("grade");
		Sheet gradeSheet = wb.getSheet(sheetName);
		
		int grCount = gradeSheet.getPhysicalNumberOfRows();
		String[] grades = new String[grCount - 1];
		
		for (int i=1; i<grCount; i++) {
			Cell gcell = gradeSheet.getRow(i).getCell(0);
			String grade = (gcell != null ? gcell.toString() : "");
//			verify.isEmpty(sheetName, readTitle(gradeSheet, 0), grade);
			
			grades[i-1] = grade;
		}
		school.setGrade(grades);
		sheetNames.remove(sheetName);
	}
	
	private static School readShoolSheet(Workbook wb, List<String> sheetNames,
			VerifyUtils verify) {
		String sheetName = sheetNameMap.get("school");

		Sheet scSheet = wb.getSheet(sheetName);
		School school = new School();
		
		int rowsCount = scSheet.getPhysicalNumberOfRows();
		for (int i=1; i<rowsCount; i++) {
			
		}
		//skip title
		Row row = scSheet.getRow(1);
		if (row != null) {
			Cell c0 = row.getCell(0);
			Cell c1 = row.getCell(1);
			
			String name = (c0 != null ? c0.toString() : "");
//			verify.isEmpty(sheetName, readTitle(scSheet, 0), name);
			
			String address = (c1 != null ? c1.toString() : "");
//			verify.isEmpty(sheetName, readTitle(scSheet, 1), address);

			school.setName(name);
			school.setAddress(address);
			sheetNames.remove(sheetName);
		}
		return school;
	}
	
	private static Multimap<String, User> readUserInfo(Workbook wb, List<String> sheetNames,
			VerifyUtils verify) {
		Multimap<String, User> userMaps = ArrayListMultimap.create();
		
		for (String classSign : sheetNames) {
			Sheet sheet = wb.getSheet(classSign);
			int rowCount = sheet.getPhysicalNumberOfRows();
			
			for (int i=1; i<rowCount; i++) {
				Row row = sheet.getRow(i);
				List<String> stds = readDatas(verify, sheet, row);
				
				User u = new User();
				u.setStudentName(stds.get(0));
				u.setSex(stds.get(1));
				u.setAge(stds.get(2));
				u.setParentName(stds.get(3));
				
				String pNum = stds.get(4);
//				verify.isCellPhoneExist(classSign, pNum);
				u.setCellPhone(pNum);
				
				u.setRelationship(stds.get(5));
				
				u.setAccountType(stds.get(6));
				userMaps.put(classSign, u);
			}
		}
		return userMaps;
	}
	
	private static Multimap<String, Teacher> readTeacherInfo(Workbook wb, List<String> sheetNames,
			VerifyUtils verify) {
		Multimap<String, Teacher> tMaps = ArrayListMultimap.create();
		
		String sheetName = sheetNameMap.get("teacher");
		Sheet tSheet = wb.getSheet(sheetName);
		int tCount = tSheet.getPhysicalNumberOfRows();
		
		for (int i=1; i<tCount; i++) {
			Row row = tSheet.getRow(i);
			Teacher t = new Teacher();
			List<String> values = readDatas(verify, tSheet, row);
			
			t.setName(values.get(0));
			
			String phoneNumber = values.get(1);
//			verify.isCellPhoneExist(sheetName, phoneNumber);
			t.setPhoneNumber(phoneNumber);
			
			t.setRole(values.get(2));
			tMaps.put(sheetName, t);
		}
		
		sheetNames.remove(sheetName);
		
		return tMaps;
	}
	
	//Get all Data inclue titles
	private static Map<String, Table<String, String, String>> readTeacherInfo1(Workbook wb) {
		Map<String, Table<String, String, String>> allDatas = Maps.newHashMap();
		int sheetCount = wb.getNumberOfSheets();
		
		for (int i=0; i<sheetCount; i++) {
			String sheetName = wb.getSheetName(i);
			Table<String, String, String> datas = readDatas(wb.getSheet(sheetName));
			allDatas.put(sheetName, datas);
		}
		
		return allDatas;
	}
	
	private static Table<String, String, String> readDatas(Sheet sheet) {
		Table<String, String, String> sheetTable = HashBasedTable.create();
		int columnCount = 0;
		
		int rowCount = sheet.getPhysicalNumberOfRows();
		for (int i=0; i<rowCount; i++) {
			Row r = sheet.getRow(i);
			
			if (i == 0) {
				columnCount = r.getPhysicalNumberOfCells(); 
			} 
			
			//row empty flag!
			int emptyCount = 0;
			
			//read cells
			for (int j=0; j<columnCount; j++) {
				Cell c = r.getCell(j);
				if (i == 0 && c == null) {
					columnCount --;
					continue;
				}
				
				if (i !=0 && (c == null || StringUtils.isEmpty(c.toString()))) {
					System.out.println("" + i + " " + j + " is empty!");
					emptyCount ++;
				}
				sheetTable.put(String.valueOf(i), String.valueOf(j), (c != null ? c.toString() : ""));
			}
			
			//remove empty row
			if (emptyCount == columnCount) {
				for (int k=0; k<columnCount; k++) {
					sheetTable.remove("" + i, "" + k);
				}
				
			}
			
		}
		
		System.out.println(sheetTable.size());
		return sheetTable;
	}
	

}
