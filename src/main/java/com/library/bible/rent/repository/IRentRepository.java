package com.library.bible.rent.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.library.bible.rent.model.Rent;

@Mapper
@Repository
public interface IRentRepository {
	
	//Get all rent list
	List<Rent> selectAllRent();
	
	//Rent table CRUD
	Rent selectRent(int rentId);
	void insertRent(Rent rent);
	void updateRent(Rent rent);
	int deleteRent(int rentId);
}
