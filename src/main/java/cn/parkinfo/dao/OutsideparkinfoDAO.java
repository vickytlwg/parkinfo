package cn.parkinfo.dao;

import org.apache.ibatis.annotations.Param;

import cn.parkinfo.model.Outsideparkinfo;

public interface OutsideparkinfoDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(Outsideparkinfo record);

    int insertSelective(Outsideparkinfo record);

    Outsideparkinfo selectByPrimaryKey(Integer id);
    
    Outsideparkinfo getByParkidAndDate(@Param("parkId")int parkId,@Param("date")String date);

    int updateByPrimaryKeySelective(Outsideparkinfo record);

    int updateByPrimaryKey(Outsideparkinfo record);
}