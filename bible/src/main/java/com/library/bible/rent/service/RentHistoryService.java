package com.library.bible.rent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.library.bible.rent.model.RentHistory;
import com.library.bible.rent.repository.IRentHistoryRepository;

import jakarta.transaction.Transactional;

@Service
public class RentHistoryService implements IRentHistoryService {
	
	@Autowired
	private IRentHistoryRepository rentHistoryRepository;

	@Override
	@Cacheable(value="rentHistorys")
	public List<RentHistory> selectAllRentHistory() {
		return rentHistoryRepository.selectAllRentHistory();
	}

	@Override
	@Cacheable(value="rentHistory", key="#rentHistoryId")
	public RentHistory selectRentHistory(int rentHistoryId) {
		return rentHistoryRepository.selectRentHistory(rentHistoryId);
	}

	@Override
	@Transactional
	@CacheEvict(value = "rentHistorys", allEntries = true)
	public void insertRentHistory(RentHistory rentHistory) {
		rentHistoryRepository.insertRentHistory(rentHistory);
	}

	@Override
	@Transactional
	@CachePut(value = "rentHistory", key = "#rentHistory.rentHistoryId")
	public void updateRentHistory(RentHistory rentHistory) {
		rentHistoryRepository.updateRentHistory(rentHistory);
	}

	@Override
	@Transactional
	@Caching(evict= {
		@CacheEvict(value = "rentHistory", key = "#rentHistoryId"),
		@CacheEvict(value = "rentHistorys", allEntries = true)
	})
	public int deleteRentHistory(int rentHistoryId) {
		return rentHistoryRepository.deleteRentHistory(rentHistoryId);
	}
}
