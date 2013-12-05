package utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTool {

	public static void main(String[] args) {   
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");  
	    TimeZone t = sdf.getTimeZone();  
	    t.setRawOffset(0);  
	    sdf.setTimeZone(t);  
	    Long startTime = System.currentTimeMillis();  
	    String fileName = "C:/Users/hanchao.SCRC/Desktop/icon/test.xlsx";  
	    // 检测代码  
	    try {  
	        // 读取excel2007  
	    	ExcelTool.poiReadExcel(fileName);  
	    } catch (Exception ex) {  
	    }  
	    Long endTime = System.currentTimeMillis();  
	    System.out.println("用时：" + sdf.format(new Date(endTime - startTime)));
	}
	
	public static List<String> poiReadExcel(String fileName) throws IOException{
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径  
		XSSFWorkbook xwb = new XSSFWorkbook(fileName);  
		// 读取第一章表格内容  
		XSSFSheet sheet = xwb.getSheetAt(0);  
		// 定义 row、cell  
		XSSFRow row;  
		XSSFCell cell;
		String str;
		List<String > result = new ArrayList<String>();
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
		    row = sheet.getRow(i);
		    for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
		        // 通过 row.getCell(j).toString() 获取单元格内容，
		    	cell = row.getCell(j);
		    	switch(cell.getCellType()){
		    		case XSSFCell.CELL_TYPE_NUMERIC:   //数字
		    			Double doubleValue = cell.getNumericCellValue();
		    			str = doubleValue.toString();
		    			if(str.contains("E")){
		    				BigDecimal bd = new BigDecimal(str);
		    				str = bd.toPlainString();
		    			}
		    			if(str.contains(".0"))str = str.replace(".0", "");
		    			result.add(str);
		    			break;
		    		case XSSFCell.CELL_TYPE_STRING:    //字符串
		    			result.add(cell.getStringCellValue());;
		    			break;
		    	}
		    }
		}
		return result;
	}
}