package cn.joymates.infoin.utils;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

/**
 * excel file verify uitl
 * 
 * @author Jackie Hou
 *
 */
public class VerifyUtils {
	
	private List<String> ignoreItems = Lists.newArrayList("主账号");
	/**
	 * verify empty
	 * @param content
	 * @return
	 */
	public boolean isEmpty(String sheetName, String rowNumber, String columnName, String content) {
		if (ignoreItems.contains(columnName)) {
			return false;
		}
		
		if (StringUtils.isEmpty(content)) {
			errorMsgInfo.put(sheetName, "Row " + rowNumber + ": " + columnName + " is empty!");
			return true;
		}
		return false;
	}
	
	
	/**
	 * verify cell phone number is already exist.
	 * @param phoneNumber
	 * @return
	 */
	public boolean isCellPhoneExist(String sheetName, String rowNumber, String columnName, String phoneNumber) {
		if (numbers == null) {
			loadAllUserPhoneNumber();
			Collections.sort(numbers);
		}
		
		if (Collections.binarySearch(numbers, phoneNumber) > 0) {
			errorMsgInfo.put(sheetName, "Row " + rowNumber + ": " + columnName  + ":" +phoneNumber + " is already exist!");
			return true;
		}
		
		return false;
	}
	
	private void loadAllUserPhoneNumber() {
		SqlSession sess = SessionFactoryUtil.getSession();
		numbers = sess.selectList("search.findAllPhoneNumber");
		SessionFactoryUtil.closeSession();
	}
	
	public VerifyUtils() {
		errorMsgInfo = ArrayListMultimap.create();
	}
	
	private List<String> numbers;
	
	/**
	 * error info map
	 */
	private ListMultimap<String, String> errorMsgInfo;
	
	public ListMultimap<String, String> getErrorMsgInfo() {
		return errorMsgInfo;
	}
	
}
