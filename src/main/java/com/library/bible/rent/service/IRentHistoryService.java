package com.library.bible.rent.service;

import java.util.List;

import com.library.bible.rent.model.RentHistory;

public interface IRentHistoryService {
	List<RentHistory> selectAllRentHistory();
	
	RentHistory selectRentHistory(int rentHistoryId);
	void insertRentHistory(RentHistory rentHistory);
	void updateRentHistory(RentHistory rentHistory);
	int deleteRentHistory(int rentHistoryId);
}
