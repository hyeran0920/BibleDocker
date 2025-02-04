package com.library.bible.reservation.controller;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.bible.reservation.model.Reservation;
import com.library.bible.reservation.service.IReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reserv")
public class ReservationController {
	private final IReservationService reservService;
	private final ReentrantLock lock = new ReentrantLock();		//동시성 lock 처리
	
	//예약 전체 조회
	@GetMapping
	public ResponseEntity<List<Reservation>> selectAllReserv(){
		List<Reservation> reservs = reservService.selectAllReserv();
		if(reservs.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(reservs);
	}
	
	//특정 예약 조회
	@GetMapping("{reservId}")
	public ResponseEntity<Reservation> selectReserv(@PathVariable long reservId){
		Reservation reserv = reservService.selectReserv(reservId);
		if(reserv == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(reserv);
	}
	
	//예약 생성
	@PostMapping
	public ResponseEntity<Reservation> insertReserv(@RequestBody @Validated Reservation reservation){
		lock.lock();
		try {
			reservService.insertReserv(reservation);
			return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
		} finally {
			lock.unlock();
		}
	}
	
	//예약 수정
	@PutMapping("{reservId}")
	public ResponseEntity<Reservation> updateReserv(@PathVariable long reservId, @RequestBody Reservation reservation){
		Reservation existingReserv = reservService.selectReserv(reservId);
		if(existingReserv == null) {
			return ResponseEntity.noContent().build();
		}
		reservService.updateReserv(reservation);
		return ResponseEntity.ok(reservation);
	}
	
	//예약 삭제
	@DeleteMapping("{reservId}")
	public void deleteReserv(@PathVariable long reservId) {
		reservService.deleteReserv(reservId);
	}
}
