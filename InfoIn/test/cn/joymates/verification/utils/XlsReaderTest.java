package cn.joymates.verification.utils;

import org.junit.Test;

import cn.joymates.infoin.utils.XlsReader;


public class XlsReaderTest {
	
	@Test
	public void testLoadXls() {
		XlsReader.sheetNameMap.put("school", "学校");
		XlsReader.sheetNameMap.put("grade", "年级");
		XlsReader.sheetNameMap.put("classes", "班级");
		
		XlsReader.sheetNameMap.put("teacher", "教师");
		XlsReader.sheetNameMap.put("term", "学期");
		
		XlsReader.readExcelFile("c:/testXls/学校信息.xls", "学校信息.xls");
//		XlsReader.readExcelFile("c:/testXls/dayi.xlsx", "dayi.xlsx");
//		XlsReader.readExcelFile("c:/testXls/2007.xlsx");
	}
	
//	@Test
//	public void testLoadXlsx() {
//		XlsReader.readXlsxOf2007("c:/testXls/2007.xlsx");
//	}
}
