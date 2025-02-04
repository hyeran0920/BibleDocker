package com.library.bible.rent.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.library.bible.rent.model.RentHistory;

@Mapper
@Repository
public interface IRentHistoryRepository {
	//Get all rent list
	List<RentHistory> selectAllRentHistory();
	
	//Rent table CRUD
	RentHistory selectRentHistory(int rentHistoryId);
	void insertRentHistory(RentHistory rentHistory);
	void updateRentHistory(RentHistory rentHistory);
	int deleteRentHistory(int rentHistoryId);
}
