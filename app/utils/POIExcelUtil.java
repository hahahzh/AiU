package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <ul>
 * <li>Title:[POI基础上的Excel数据读取工具]</li>
 * <li>Description: [支持Excell2003,Excell2007,自动格式化数值型数据,自动格式化日期型数据]</li>
 * <li>Copyright 2009 RoadWay Co., Ltd.</li>
 * <li>All right reserved.</li>
 * <li>Created by [惠万鹏] [Jan 20, 2010]</li>
 * <li>Midified by [modifier] [modified time]</li>
 * 
 * <li>所需Jar包列表</li>
 * <li>poi-3.6-20091214.jar</li>
 * <li>poi-contrib-3.6-20091214.jar</li>
 * <li>poi-examples-3.6-20091214.jar</li>
 * <li>poi-ooxml-3.6-20091214.jar</li>
 * <li>poi-ooxml-schemas-3.6-20091214.jar</li>
 * <li>poi-scratchpad-3.6-20091214.jar</li>
 * <li>xmlbeans-2.3.0.jar</li>
 * <ul>
 * 
 * @version 1.0
 */
public class POIExcelUtil {
	/** 总行数 */
	private int totalRows = 0;

	/** 总列数 */
	private int totalCells = 0;

	/** 构造方法 */
	public POIExcelUtil() {
	}

	/**
	 * <ul>
	 * <li>Description:[根据文件名读取excel文件]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public List<ArrayList<String>> read(String fileName) {
		List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();

		/** 检查文件名是否为空或者是否是Excel格式的文件 */
		if (fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
			return dataLst;
		}

		boolean isExcel2003 = true;
		/** 对文件的合法性进行验证 */
		if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}

		/** 检查文件是否存在 */
		File file = new File(fileName);
		if (file == null || !file.exists()) {
			return dataLst;
		}

		try {
			/** 调用本类提供的根据流读取的方法 */
			dataLst = read(new FileInputStream(file), isExcel2003);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/** 返回最后读取的结果 */
		return dataLst;
	}

	/**
	 * <ul>
	 * <li>Description:[根据流读取Excel文件]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @param inputStream
	 * @param isExcel2003
	 * @return
	 */
	public List<ArrayList<String>> read(InputStream inputStream,
			boolean isExcel2003) {
		List<ArrayList<String>> dataLst = null;
		try {
			/** 根据版本选择创建Workbook的方式 */
			Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream)
					: new XSSFWorkbook(inputStream);
			dataLst = read(wb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataLst;
	}

	/**
	 * <ul>
	 * <li>Description:[得到总行数]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @return
	 */
	public int getTotalRows() {
		return totalRows;
	}

	/**
	 * <ul>
	 * <li>Description:[得到总列数]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @return
	 */
	public int getTotalCells() {
		return totalCells;
	}

	/**
	 * <ul>
	 * <li>Description:[读取数据]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @param wb
	 * @return
	 */
	private List<ArrayList<String>> read(Workbook wb) {
		List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();

		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(0);
		this.totalRows = sheet.getPhysicalNumberOfRows();
		if (this.totalRows >= 1 && sheet.getRow(0) != null) {
			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}

		/** 循环Excel的行 */
		for (int r = 0; r < this.totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}

			ArrayList<String> rowLst = new ArrayList<String>();
			/** 循环Excel的列 */
			for (short c = 0; c < this.getTotalCells(); c++) {
				Cell cell = row.getCell(c);
				String cellValue = "";
				if (cell == null) {
					rowLst.add(cellValue);
					continue;
				}

				/** 处理数字型的,自动去零 */
				if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {

					cellValue = getRightStr(cell.getNumericCellValue() + "");
				}
				/** 处理字符串型 */
				else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
					cellValue = cell.getStringCellValue();
				}
				/** 处理布尔型 */
				else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
					cellValue = cell.getBooleanCellValue() + "";
				}
				/** 其它的,非以上几种数据类型 */
				else {
					cellValue = cell.toString() + "";
				}

				rowLst.add(cellValue);
			}
			dataLst.add(rowLst);
		}
		return dataLst;
	}

	/**
	 * <ul>
	 * <li>Description:[正确地处理整数后自动加零的情况]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @param sNum
	 * @return
	 */
	private String getRightStr(String sNum) {
		DecimalFormat decimalFormat = new DecimalFormat("#.000000");
		String resultStr = decimalFormat.format(new Double(sNum));
		if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
			resultStr = resultStr.substring(0, resultStr.indexOf("."));
		}
		return resultStr;
	}

	public static List<String> poiReadExcel(String fileName) throws IOException {
		List<ArrayList<String>> dataLst = new POIExcelUtil().read(fileName);
		List<String> l = new ArrayList<String>();
		for (ArrayList<String> innerLst : dataLst) {
			StringBuffer rowData = new StringBuffer();
			for (String dataStr : innerLst) {
				rowData.append(",").append(dataStr);
			}
			if (rowData.length() > 0) {
				l.add(rowData.deleteCharAt(0).toString());
			}
		}
		return l;
	}

	/**
	 * <ul>
	 * <li>Description:[测试main方法]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		List<ArrayList<String>> dataLst = new POIExcelUtil()
				.read("e:/test.xls");
		for (ArrayList<String> innerLst : dataLst) {
			StringBuffer rowData = new StringBuffer();
			for (String dataStr : innerLst) {
				rowData.append(",").append(dataStr);
			}
			if (rowData.length() > 0) {
				System.out.println(rowData.deleteCharAt(0).toString());
			}
		}
	}
}