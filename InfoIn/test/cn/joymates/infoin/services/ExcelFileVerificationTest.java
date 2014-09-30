package cn.joymates.infoin.services;

import java.util.Map;

import org.junit.Test;

import cn.joymates.infoin.utils.VerifyUtils;
import cn.joymates.infoin.utils.XlsReader;

import com.google.common.collect.Table;


public class ExcelFileVerificationTest {
	
	@Test
	public void testExcelFileVerification() {
		IExcelFileVerification v = new ExcelFileVerificationImpl();
		Map<String, Table<String, String, String>> dataMap = XlsReader.readExcelFile("c:/testXls/学校信息.xls", "学校信息.xls");
		VerifyUtils vv = v.verify(dataMap);
		
		vv.getErrorMsgInfo().size();
	}
}
