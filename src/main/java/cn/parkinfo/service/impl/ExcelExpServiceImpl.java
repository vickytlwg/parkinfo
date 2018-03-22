package cn.parkinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import cn.parkinfo.model.ExcelExp;
import cn.parkinfo.model.ReadExcel;
import cn.parkinfo.service.ExcelExpService;
@Service
public class ExcelExpServiceImpl implements ExcelExpService{

	@Override
	public String readExcelFile(MultipartFile file) {
		// TODO Auto-generated method stub
		String result="";
		//创建处理EXCEL的类  
        ReadExcel readExcel=new ReadExcel();  
        //解析excel，获取上传的事件单  
        List<ExcelExp> expList = readExcel.getExcelInfo(file);  
        //将excel中的数据转换到list,可以操作list,进行保存到数据库
        if(expList != null && !expList.isEmpty()){  
            result = "上传成功";  
        }else{  
            result = "上传失败";  
        }  
        return result;  
	}

	
}
