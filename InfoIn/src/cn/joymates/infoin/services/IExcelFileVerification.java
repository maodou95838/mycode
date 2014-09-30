package cn.joymates.infoin.services;

import java.util.Map;

import cn.joymates.infoin.utils.VerifyUtils;

import com.google.common.collect.Table;

/**
 * excel file 
 * @author Jackie Hou
 *
 */
public interface IExcelFileVerification {
	VerifyUtils verify(Map<String, Table<String, String, String>> sheetMap);
}
