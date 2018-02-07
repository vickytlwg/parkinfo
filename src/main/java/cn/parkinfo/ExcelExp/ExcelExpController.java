package cn.parkinfo.ExcelExp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/import") 
public class ExcelExpController{
	
	@Autowired
	private ExcelExpService excelExpService;
	
	 /**
     * 导入Excel功能
     *
     * @param file
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/importExcel")
    @ResponseBody
    public int importExcel(@RequestParam("file") MultipartFile file,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        int flag = 0;//上传标志
        if (!file.isEmpty()) {
            String clientFileName = file.getOriginalFilename();
            System.out.println("clientFileName:" + clientFileName);
            String path = request.getSession().getServletContext().getRealPath("/uploadExcel");
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }

            String fileName = path + "/" + clientFileName;
            System.out.println("fileName:" + fileName);
            File saveFile = new File(fileName);
            System.out.println("saveFile:" + saveFile);
            file.transferTo(saveFile);

            List<ExcelExp> list_pop = readExcelFile(fileName, request.getSession());
            System.out.println("list_pop_size:" + list_pop.size());
            for (ExcelExp pop : list_pop) {
                flag = this.excelExpService.addExcelpark(pop);
            }
        }
        return flag > 0 ? 1 : 0;
    }
    
    
    /**
     * 根据Excel文件路径，解析Excel
     *
     * @param filePath
     * @return list
     */
    public List<ExcelExp> readExcelFile(String filePath, HttpSession session) {

        File file = new File(filePath);
        List<ExcelExp> list = new ArrayList<ExcelExp>();
        ExcelExp excelExp = null;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                HSSFSheet sheet = workbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    HSSFRow row = sheet.getRow(rowNum);
                    excelExp = new ExcelExp();
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        HSSFCell cell = row.getCell(i);
                        cell.setCellType(Cell.CELL_TYPE_STRING);
//                        cell.setCellType(CellType.STRING);
                        //车主
                        String owner = row.getCell(1).getStringCellValue();
                        excelExp.setOwner(String.valueOf(owner.trim()));
                        //车牌号
                        String cardnumber = row.getCell(2).getStringCellValue();
                        excelExp.setCardnumber(String.valueOf(cardnumber.trim()));
                        //开始日期
                        String starttime = row.getCell(3).getStringCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);  
                        Date date=null;
                        try {
                            date = sdf.parse(starttime);
                            excelExp.setStarttime(date);
                        } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//                        excelExp.setStarttime(sdf.parse((String) starttime.trim()));
                        //结束日期
                        String endtime = row.getCell(4).getStringCellValue();
                        try {
                            date = sdf.parse(endtime);
                            excelExp.setEndtime(date);
                        } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//                        excelExp.setStarttime(sdf.parse((String) endtime.trim()));
                        //状态
                        String status = row.getCell(5).getStringCellValue();
                        switch (status) {
                            case "0":
                            	status = "1";
                                break;
                            case "1":
                            	status = "2";
                                break;
                        }
                        excelExp.setStatus(Integer.valueOf(status.trim()));
                    }
                    list.add(excelExp);
                }
                System.out.println("成功导入Excel数据" + excelExp + "!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
                        
//	@Autowired
//	private ExcelExpService excelExpService;
//	
//	/**  
//     * 描述：通过form表单提交方式导入excel文件  
//     * @param request  
//     * @throws Exception  
//     */  
//    @RequestMapping(value="/upload",method=RequestMethod.POST)  
//    @ResponseBody
//    public String upload(@RequestParam(value="file",required = false)MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
//    	String result = excelExpService.readExcelFile(file); 
//    	System.out.println(result+"aaaaaaaaaaaaaa");
//        return result;   
//    }  

