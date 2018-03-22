package cn.parkinfo.service;

import org.springframework.web.multipart.MultipartFile;

import cn.parkinfo.model.ExcelExp;

public interface ExcelExpService {
//	public int addExcelpark(ExcelExp excelExp);
	
	
//	/** 
//     * 读取excel中的数据,生成list 
//     */ 
	public String readExcelFile( MultipartFile file);
	
//	public List<ExcelExp> findAllExcelExp(Map<String, Object> map);
}
