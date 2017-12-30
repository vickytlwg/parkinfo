package cn.parkinfo.service;

import java.util.List;

public interface UserParkService {

	public List<Integer> getOwnParkId(int userId);
}
