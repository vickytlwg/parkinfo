package cn.parkinfo.dao;


import org.springframework.stereotype.Repository;

import cn.parkinfo.model.ParkNews;

@Repository
public interface ParkNewsDAO {
	
	public ParkNews getLatestParkNews(int parkId);
	
	public int insertParkNews(ParkNews parkNews);

}
