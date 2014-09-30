package cn.joymates.infoin.action;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import cn.joymates.infoin.services.ExcelFileVerificationImpl;
import cn.joymates.infoin.services.IExcelFileVerification;
import cn.joymates.infoin.utils.VerifyUtils;
import cn.joymates.infoin.utils.XlsReader;

import com.google.common.collect.Table;
import com.google.common.io.Files;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Excel file upload action
 * 
 * @author Jackie Hou
 * 
 */
public class XlsFileUploadAction extends ActionSupport {

	public String upload() {
		String toPath = ServletActionContext.getServletContext().getRealPath(uploadDir);
		File toDir = new File(toPath);
		if (!toDir.exists()) {
			toDir.mkdirs();
		}
		
		String info = "";
		
		String fromPath = toPath + "\\" + myFileFileName; 
		try {
			Files.copy(myFile, new File(fromPath));
			info = "文件 " + myFileFileName + "上传成功！ 准备开始校验，请稍等... ...";
			
			IExcelFileVerification v = new ExcelFileVerificationImpl();
			Map<String, Table<String, String, String>> dataMap = XlsReader.readExcelFile(fromPath, myFileFileName);
			VerifyUtils verifyInfo = v.verify(dataMap);
			
			ActionContext ctx = ActionContext.getContext();
			ctx.put("errorInfo", verifyInfo.getErrorMsgInfo().asMap());
			ctx.put("fileName", myFileFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	private File myFile;
	private String myFileFileName;
	private String myFileContentType;
	
	private String uploadDir;
	private String desc;
	
	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getMyFileContentType() {
		return myFileContentType;
	}

	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

}
