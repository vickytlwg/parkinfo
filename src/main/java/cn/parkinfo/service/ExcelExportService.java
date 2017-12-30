package cn.parkinfo.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import cn.parkinfo.model.Constants;
import cn.parkinfo.model.Monthuser;
import cn.parkinfo.model.PosChargeData;
import cn.parkinfo.model.Posdata;

@Service
public class ExcelExportService {

	public void produceExceldataPosData(String title, String[] headers, List<Posdata> dataset, XSSFWorkbook workbook) {
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为25个字节
		sheet.setDefaultColumnWidth(25);
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		for (int j = 0; j < dataset.size(); j++) {
			XSSFRow row1 = sheet.createRow(j + 1);
			Posdata posdata = dataset.get(j);

			XSSFCell cell1 = row1.createCell(0);
			cell1.setCellStyle(style2);
			cell1.setCellValue(posdata.getCardsnr());

			XSSFCell cell2 = row1.createCell(1);
			cell2.setCellStyle(style2);
			cell2.setCellValue(posdata.getSitename());

			XSSFCell cell3 = row1.createCell(2);
			cell3.setCellStyle(style2);
			cell3.setCellValue(posdata.getBackbyte());

			XSSFCell cell5 = row1.createCell(3);
			cell5.setCellStyle(style2);
			if (posdata.getIsarrearage() == false) {
				cell5.setCellValue(posdata.getMode() == 0 ? "进场" : "出场");
			} else {
				cell5.setCellValue("补交");
			}

			XSSFCell cell6 = row1.createCell(4);
			cell6.setCellStyle(style2);
			cell6.setCellValue(posdata.getUserid());

			XSSFCell cell7 = row1.createCell(5);
			cell7.setCellStyle(style2);
			cell7.setCellValue(posdata.getPossnr());

			XSSFCell cell8 = row1.createCell(6);
			cell8.setCellStyle(style2);
			cell8.setCellValue(Float.parseFloat(posdata.getMoney().toString()));

			XSSFCell cell9 = row1.createCell(7);
			cell9.setCellStyle(style2);
			cell9.setCellValue(Float.parseFloat(posdata.getGiving().toString()));

			XSSFCell cell10 = row1.createCell(8);
			cell10.setCellStyle(style2);
			cell10.setCellValue(Float.parseFloat(posdata.getRealmoney().toString()));

			XSSFCell cell11 = row1.createCell(9);
			cell11.setCellStyle(style2);
			cell11.setCellValue(Float.parseFloat(posdata.getReturnmoney().toString()));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			XSSFCell cell12 = row1.createCell(10);
			cell12.setCellStyle(style2);
			cell12.setCellValue(sdf.format(posdata.getStarttime()));

			XSSFCell cell13 = row1.createCell(11);
			cell13.setCellStyle(style2);
			String date = "";
			if (posdata.getEndtime() != null) {
				date = sdf.format(posdata.getEndtime());
			}
			cell13.setCellValue(date);

		}
	}

	public void produceExceldataPosChargeData(String title, String[] headers, List<PosChargeData> dataset,
			XSSFWorkbook workbook) {
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为25个字节
		sheet.setDefaultColumnWidth(25);
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		for (int j = 0; j < dataset.size(); j++) {
			XSSFRow row1 = sheet.createRow(j + 1);
			PosChargeData posdata = dataset.get(j);

			XSSFCell cell1 = row1.createCell(0);
			cell1.setCellStyle(style2);
			cell1.setCellValue(posdata.getCardNumber());

			XSSFCell cell2 = row1.createCell(1);
			cell2.setCellStyle(style2);
			cell2.setCellValue(posdata.getParkDesc());

			XSSFCell cell3 = row1.createCell(2);
			cell3.setCellStyle(style2);
			cell3.setCellValue(posdata.getPortNumber());

			XSSFCell cell4 = row1.createCell(3);
			cell4.setCellStyle(style2);
			cell4.setCellValue(posdata.getOperatorId());

			XSSFCell cell5 = row1.createCell(4);
			cell5.setCellStyle(style2);
			cell5.setCellValue(posdata.isPaidCompleted() == false ? "未收费" : "已收费");

			XSSFCell cell6 = row1.createCell(5);
			cell6.setCellStyle(style2);
			cell6.setCellValue(posdata.getPaidMoney());

			XSSFCell cell7 = row1.createCell(6);
			cell7.setCellStyle(style2);
			cell7.setCellValue(posdata.getChargeMoney());

			XSSFCell cell8 = row1.createCell(7);
			cell8.setCellStyle(style2);
			cell8.setCellValue(posdata.getGivenMoney());

			XSSFCell cell9 = row1.createCell(8);
			cell9.setCellStyle(style2);
			cell9.setCellValue(posdata.getChangeMoney());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			XSSFCell cell10 = row1.createCell(9);
			cell10.setCellStyle(style2);
			cell10.setCellValue(sdf.format(posdata.getEntranceDate()));

			XSSFCell cell11 = row1.createCell(10);
			cell11.setCellStyle(style2);
			if (posdata.getExitDate() != null) {
				cell11.setCellValue(sdf.format(posdata.getExitDate()));
			} else {
				cell11.setCellValue("");
			}
		}
	}

	public void produceExceldataMonthUser(String title, String[] headers, List<Monthuser> dataset,
			XSSFWorkbook workbook) {
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为25个字节
		sheet.setDefaultColumnWidth(25);
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		for (int j = 0; j < dataset.size(); j++) {
			XSSFRow row1 = sheet.createRow(j + 1);
			Monthuser posdata = dataset.get(j);

			XSSFCell cell1 = row1.createCell(0);
			cell1.setCellStyle(style2);
			cell1.setCellValue(posdata.getParkname());

			XSSFCell cell2 = row1.createCell(1);
			cell2.setCellStyle(style2);
			cell2.setCellValue(posdata.getCardnumber());

			XSSFCell cell3 = row1.createCell(2);
			cell3.setCellStyle(style2);
			cell3.setCellValue(posdata.getOwner());

			XSSFCell cell4 = row1.createCell(3);
			cell4.setCellStyle(style2);
			cell4.setCellValue(posdata.getPlatenumber());

			XSSFCell cell5 = row1.createCell(4);
			cell5.setCellStyle(style2);
			cell5.setCellValue(posdata.getCertificatetype());

			XSSFCell cell6 = row1.createCell(5);
			cell6.setCellStyle(style2);
			cell6.setCellValue(posdata.getCertificatenumber());

			XSSFCell cell7 = row1.createCell(6);
			cell7.setCellStyle(style2);
			cell7.setCellValue("");
			if (posdata.getStarttime()!=null) {
				cell7.setCellValue(new SimpleDateFormat(Constants.DATEFORMAT).format(posdata.getStarttime()));
			}

			XSSFCell cell8 = row1.createCell(7);
			cell8.setCellStyle(style2);
			cell8.setCellValue("");
			if (posdata.getEndtime()!=null) {
			cell8.setCellValue(new SimpleDateFormat(Constants.DATEFORMAT).format(posdata.getEndtime()));
			}
			
			XSSFCell cell9 = row1.createCell(8);
			cell9.setCellStyle(style2);
			cell9.setCellValue("");
			if (posdata.getPayment()!=null) {
			cell9.setCellValue(posdata.getPayment());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			XSSFCell cell10 = row1.createCell(9);
			cell10.setCellStyle(style2);
			cell10.setCellValue("");
			if (posdata.getStatus()!=null) {
				cell10.setCellValue(posdata.getStatus()==1?"已支付":"未支付");
			}
			


		}

	}

}
