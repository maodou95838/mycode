package cn.joymates.infoin.services;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.joymates.infoin.utils.VerifyUtils;

import com.google.common.collect.Table;

public class ExcelFileVerificationImpl implements IExcelFileVerification {
	private static Logger log = Logger.getLogger(ExcelFileVerificationImpl.class);
	
	@Override
	public VerifyUtils verify(Map<String, Table<String, String, String>> sheetMap) {
		log.info("Ready for verifing datas!");
		VerifyUtils verify = new VerifyUtils();
		
		for (Map.Entry<String, Table<String, String, String>> entry : sheetMap.entrySet()) {
			String sheetName = entry.getKey();
			Table<String, String, String> table = entry.getValue();
			
			Set<String> cSet = table.columnKeySet();
			Set<String> rSet = table.rowKeySet();
			
			log.debug("start to verify " + sheetName);
			for (String rkey : rSet) {
				for (String ckey : cSet) {
					String v = table.get(rkey, ckey);
					
					//skip title
					if (rkey.equals("1")) {
						continue;
					}
					
					log.debug("verify cell(" + rkey + ", " + ckey + ")=" + v);
					if (verify.isEmpty(sheetName, rkey, ckey, v)) {
						continue;
					}
					
					if (ckey.equals("电话")) {
						verify.isCellPhoneExist(sheetName, rkey, ckey, v);
					}
				}
			}
			log.debug("verify complete!");
			
		}		
		return verify;
		
	}
		
}
