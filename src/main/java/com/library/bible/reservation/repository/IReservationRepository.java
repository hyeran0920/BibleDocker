package com.library.bible.reservation.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.library.bible.reservation.model.Reservation;

@Mapper
@Repository
public interface IReservationRepository {
	//Get all reservation list
	List<Reservation> selectAllReserv();
	
	//Reservation CRUD
	Reservation selectReserv(long reservId);
	void insertReserv(Reservation reservation);
	void updateReserv(Reservation reservation);
	int deleteReserv(long reservId);
}
