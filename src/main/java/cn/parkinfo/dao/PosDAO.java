package cn.parkinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.parkinfo.model.Pos;
@Repository
public interface PosDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(Pos record);

    int insertSelective(Pos record);

    Pos selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pos record);

    int updateByPrimaryKey(Pos record);
    
    int getCount();
    
    List<Pos> getByStartAndCount(@Param("start")int start,@Param("count")int count);
    
    List<Pos> getByNum(@Param("num")String num);
    
    List<Pos> getByParkNameAndNumber(@Param("parkName")String parkName,@Param("num")String num);
}