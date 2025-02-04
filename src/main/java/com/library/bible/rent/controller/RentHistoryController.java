package com.library.bible.rent.controller;

import java.util.List;

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

import com.library.bible.rent.model.RentHistory;
import com.library.bible.rent.service.IRentHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/rent/history")
public class RentHistoryController {
	private final IRentHistoryService rentHistoryService;
	
	//대여 기록 전제 조회
	@GetMapping
	public ResponseEntity<List<RentHistory>> selectAllRentHistory(){
		List<RentHistory> historys = rentHistoryService.selectAllRentHistory();
		if(historys.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(historys);
	}
	
	//특정 대여 기록 조회
	@GetMapping("{rentHistoryId}")
	public ResponseEntity<RentHistory> selectRentHistory(@PathVariable int rentHistoryId){
		RentHistory history = rentHistoryService.selectRentHistory(rentHistoryId);
		if(history == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(history);
	}
	
	//대여 기록 생성
	@PostMapping
	public ResponseEntity<RentHistory> insertRentHistory(@RequestBody @Validated RentHistory history){
		rentHistoryService.insertRentHistory(history);
		return ResponseEntity.status(HttpStatus.CREATED).body(history);
	}
	
	//대여 기록 수정
	@PutMapping("{rentHistoryId}")
	public ResponseEntity<RentHistory> updateRentHistory(@PathVariable int rentHistoryId, @RequestBody RentHistory rentHistory){
		RentHistory existing = rentHistoryService.selectRentHistory(rentHistoryId);
		if(existing == null) {
			return ResponseEntity.notFound().build();
		}
		rentHistoryService.updateRentHistory(rentHistory);
		return ResponseEntity.ok(rentHistory);
	}
	
	//대여 삭제
	@DeleteMapping("{rentHistoryId}")
	public void deleteRentHistory(@PathVariable int rentHistoryId) {
		rentHistoryService.deleteRentHistory(rentHistoryId);
	}
}
