<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function checkFile() {
			var file = document.getElementById("file").value;
			if (file == null || file == "") {
				alert("请选择上传文件！");
				return false;
			} else {
				return true;
			}
		
		}
	</script>
  </head>
  
 <body>
 <h2><center>学校信息校验</center></h2>
 <hr/>
 <br/>
 	<center>
	   <s:form action="xlsUpload" namespace="/" method="post" enctype ="multipart/form-data" onsubmit="return checkFile()"> 
	       <s:file name ="myFile" label ="Excel File" id="file"/> 
	       <s:submit value="校验" /> 
	   </s:form >
    </center> 
    <p>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    </p>
 <s:if test="#request.errorInfo != null">   
    校验结果:  &nbsp;${fileName }
    <hr/>
    <div id="console">
    <s:if test="#request.errorInfo.size > 0">
    <center>
    <table border="2">
    	<th width="20%" align="center">sheet</th>
    	<th width="60%" align="center">error info</th>
    	<s:iterator value="#request.errorInfo" >
    		<tr>
    			<td>${key }</td>
    			<td>
    				<table>
	    			<s:iterator value="value" id="id1">
						<tr>
							<td>${id1 }</td>
						</tr>    			
					</s:iterator>					
    				</table>
    			</td>
    		</tr>
    	</s:iterator>
    	
    </table>
    </center>
    </s:if>
    <s:else>
    	未校验处错误！！！
    </s:else>
   </s:if> 
    </div>
</body >
</html>
