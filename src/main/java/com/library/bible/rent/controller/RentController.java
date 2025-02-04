package com.library.bible.rent.controller;

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

import com.library.bible.rent.model.Rent;
import com.library.bible.rent.service.IRentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/rent")
public class RentController {
	
	private final IRentService rentService;
	private final ReentrantLock lock = new ReentrantLock();		//동시성 lock 처리
	
	// 대여 전체 조회
	@GetMapping
	public ResponseEntity<List<Rent>> selectAllRent(){
		List<Rent> rents = rentService.selectAllRent();
		//데이터가 없을 경우 204 no Content 반환
		if(rents.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(rents);
	}
	
	//특정 대여 조회
	@GetMapping("{rentId}")
	public ResponseEntity<Rent> selectRent(@PathVariable int rentId){
		Rent rent =rentService.selectRent(rentId);
		if(rent == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(rent);
	}
	
	//대여 생성
	@PostMapping
	public ResponseEntity<Rent> insertRent(@RequestBody @Validated Rent rent) {
		//중복 대여 방지
		lock.lock();
		try {
			rentService.insertRent(rent);
			return ResponseEntity.status(HttpStatus.CREATED).body(rent);
		} finally {
			lock.unlock();
		}
	}
	
	//대여 수정
	@PutMapping("{rentId}")
	public ResponseEntity<Rent> updateRent(@PathVariable int rentId, @RequestBody Rent rent){
		Rent existingRent = rentService.selectRent(rentId);
		//기존 데이터 유무 판단
		if(existingRent == null) {
			return ResponseEntity.notFound().build();
		}
		rentService.updateRent(rent);
		return ResponseEntity.ok(rent);
	}
	
	// 대여 삭제
	@DeleteMapping("{rentId}")
	public void deleteRent(@PathVariable int rentId) {
		rentService.deleteRent(rentId);
	}
}
