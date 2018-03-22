package cn.parkinfo.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import cn.parkinfo.dao.MonthuserDAO;
import cn.parkinfo.model.Monthuser;
import cn.parkinfo.service.ExcelExpService;

@Controller
@RequestMapping("user") 
public class ExcelExpController{
	
	@Autowired
	private ExcelExpService excelExpService;
	@Autowired
	private MonthuserDAO mdao;
	
	/**  
     * 描述：通过form表单提交方式导入excel文件  
     * @param request  
     * @throws Exception  
     */  
    @RequestMapping(value="/upload",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})  
    @ResponseBody
    public void upload(@RequestParam(value="file",required = false)MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取文件数据
			CommonsMultipartFile cmf= (CommonsMultipartFile)file;
	        DiskFileItem dfi = (DiskFileItem)cmf.getFileItem();
	        File filePath = dfi.getStoreLocation(); 
			List<Map<String, Object>> dataList=ExcelUtil.parse(filePath, dfi.getName(), 1, 0);
			for(int i=1;i<=dataList.size()-1;i++) {
				Map<String, Object> dataMap = dataList.get(i);
				String parkid=StringUtil.getString(dataMap.get("停车场名"));
				String owner=StringUtil.getString(dataMap.get("车主"));
				String platenumber =StringUtil.getString(dataMap.get("车牌号"));
				String certificatetype=StringUtil.getString(dataMap.get("描述"));
				String type=StringUtil.getString(dataMap.get("类型"));
				String starttime =StringUtil.getString(dataMap.get("开始日期"));
				String endtime =StringUtil.getString(dataMap.get("结束日期"));
				String status =StringUtil.getString(dataMap.get("状态"));
				
				List<Monthuser> listplatenumber = mdao.getByPlateNumber(platenumber);
				if(listplatenumber.size()==0){
					//添加
					Monthuser e2 = new Monthuser();
					e2.setParkid(Integer.valueOf(parkid));
					e2.setOwner(owner);
					e2.setPlatenumber(platenumber);
					e2.setCertificatetype(certificatetype);
					e2.setType(Integer.valueOf(type));
					Date date=new Date();
					long lSysTime1 = date.getTime() / 1000 ;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					Date dt = new Date(lSysTime1 * 1000);
					String a = sdf.format(dt);
					e2.setStarttime(a);
					String b = sdf.format(dt);
					e2.setEndtime(b);
					e2.setStatus(Integer.valueOf(status));
					mdao.insert(e2);
				}else{
					System.err.println("此车牌已存在！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
	
	
	 /**
     * 导入Excel功能
     *
     * @param file
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
//    @RequestMapping(value = "addExcelpark",method = RequestMethod.POST,produces={"application/json;charset=utf-8"})
//    @ResponseBody
//    public int addExcelpark(@RequestParam("file") MultipartFile file,
//                           HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        int flag = 0;//上传标志
//        if (!file.isEmpty()) {
//            String clientFileName = file.getOriginalFilename();
//            System.out.println("clientFileName:" + clientFileName);
//            String path = request.getSession().getServletContext().getRealPath("/uploadExcel");
//            if (!new File(path).exists()) {
//                new File(path).mkdirs();
//            }
//
//            String fileName = path + "/" + clientFileName;
//            System.out.println("fileName:" + fileName);
//            File saveFile = new File(fileName);
//            System.out.println("saveFile:" + saveFile);
//            file.transferTo(saveFile);
//
//            List<ExcelExp> list_pop = readExcelFile(fileName, request.getSession());
//            System.out.println("list_pop_size:" + list_pop.size());
//            for (ExcelExp pop : list_pop) {
//                flag = this.excelExpService.addExcelpark(pop);
//            }
//        }
//        return flag > 0 ? 1 : 0;
//    }
//    
//    
//    /**
//     * 根据Excel文件路径，解析Excel
//     *
//     * @param filePath
//     * @return list
//     */
//    public List<ExcelExp> readExcelFile(String filePath, HttpSession session) {
//
//        File file = new File(filePath);
//        List<ExcelExp> list = new ArrayList<ExcelExp>();
//        ExcelExp excelExp = null;
//        try {
//            HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
//            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
//                HSSFSheet sheet = workbook.getSheetAt(numSheet);
//                if (sheet == null) {
//                    continue;
//                }
//                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
//                    HSSFRow row = sheet.getRow(rowNum);
//                    excelExp = new ExcelExp();
//                    for (int i = 0; i < row.getLastCellNum(); i++) {
//                        HSSFCell cell = row.getCell(i);
//                        cell.setCellType(Cell.CELL_TYPE_STRING);
////                        cell.setCellType(CellType.STRING);
//                        //车主
//                        String owner = row.getCell(1).getStringCellValue();
//                        excelExp.setOwner(String.valueOf(owner.trim()));
//                        //车牌号
//                        String cardnumber = row.getCell(2).getStringCellValue();
//                        excelExp.setCardnumber(String.valueOf(cardnumber.trim()));
//                        //开始日期
//                        String starttime = row.getCell(3).getStringCellValue();
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);  
//                        Date date=null;
//                        try {
//                            date = sdf.parse(starttime);
//                            excelExp.setStarttime(date);
//                        } catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
////                        excelExp.setStarttime(sdf.parse((String) starttime.trim()));
//                        //结束日期
//                        String endtime = row.getCell(4).getStringCellValue();
//                        try {
//                            date = sdf.parse(endtime);
//                            excelExp.setEndtime(date);
//                        } catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
////                        excelExp.setStarttime(sdf.parse((String) endtime.trim()));
//                        //状态
//                        String status = row.getCell(5).getStringCellValue();
//                        switch (status) {
//                            case "0":
//                            	status = "1";
//                                break;
//                            case "1":
//                            	status = "2";
//                                break;
//                        }
//                        excelExp.setStatus(Integer.valueOf(status.trim()));
//                    }
//                    list.add(excelExp);
//                }
//                System.out.println("成功导入Excel数据" + excelExp + "!");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
}
