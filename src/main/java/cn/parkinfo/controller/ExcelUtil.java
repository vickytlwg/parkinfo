package cn.parkinfo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	private static Log log =LogFactory.getLog(ExcelUtil.class);
	
	private static final String EXTENSION_XLS = "xls";
    private static final String EXTENSION_XLSX = "xlsx";
	
    private static final String EXTENSION_CSV = "csv";
    
//	public static List<Map<String,Object>> parse(InputStream is,String[] headers){
//		return parse(is, 0,0);
//	}
	
	public static List<Map<String,Object>> parse(File filePath, String fileName, int start,int sheetIndex){
		try {
			Workbook workbook = null;
			InputStream is = new FileInputStream(filePath);

			if (fileName.endsWith(EXTENSION_XLS)) {
				workbook = new HSSFWorkbook(is);
			} else if (fileName.endsWith(EXTENSION_XLSX)) {
				workbook = new XSSFWorkbook(is);
			}else if (fileName.endsWith(EXTENSION_CSV)) {
				workbook = new XSSFWorkbook(is);
			}
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			List<Map<String, Object>> ens = new ArrayList<Map<String, Object>>();
			Map<String, Object> en;
			int rowNum=sheet.getLastRowNum();
			Row row;
			
			//获取header
			List<String> headers=new ArrayList<String>();
			for(int i=0;i<1;i++){
				row=sheet.getRow(i);
				if(row==null)continue;
				
				
				for(int col=0;col<row.getLastCellNum();col++){
					Cell cell = row.getCell(col);
					headers.add(getCellContent(cell));
				}
			}
			Map<String, Object> he=new HashMap<String, Object>();
			he.put("headers", headers);
			ens.add(he);
			
			//填充内容
			for(int i=start;i<=rowNum;i++){
				row=sheet.getRow(i);
				if(row==null)continue;
				en = new HashMap<String, Object>();
				ens.add(en);
				for(int col=0;col<headers.size();col++){
					Cell cell = row.getCell(col);
//					if(cell==null)continue;
					en.put(headers.get(col),getCellContent(cell));
					//System.out.println(getCellContent(cell));
				}
			}
			
			return ens;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("parse Excel exception:"+e);
			return null;
		}

	}
	
//	public static List<Map<String,String>> parse(InputStream is,String[] headers,int start,int sheetIndex){
//		try {
//			XSSFWorkbook workbook = new XSSFWorkbook(is);
//			XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
//			List<Map<String, String>> ens = new ArrayList<Map<String, String>>();
//			Map<String, String> en;
//			int rowNum=sheet.getLastRowNum();
//			Row row;
//			for(int i=start;i<=rowNum;i++){
//				row=sheet.getRow(i);
//				if(row==null)continue;
//				en = new HashMap<String, String>();
//				ens.add(en);
//				for(int col=0;col<headers.length;col++){
//					Cell cell = row.getCell(col);
////					if(cell==null)continue;
//					en.put(headers[col],getCellContent(cell));
//				}
//			}
//			return ens;
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("parse Excel exception:"+e);
//			return null;
//		}
//
//	}
	
	public static String getCellContent(Cell cell){
		if(cell==null)return "";
		switch(cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC:return new Double(cell.getNumericCellValue()).longValue()+"";
			case Cell.CELL_TYPE_FORMULA:return cell.getCellFormula();
			case Cell.CELL_TYPE_BOOLEAN:return cell.getBooleanCellValue()+"";
			case Cell.CELL_TYPE_ERROR:return "";
			default:return StringUtil.getString(cell.getStringCellValue()).trim();
		}
	}
	
	
	
}
