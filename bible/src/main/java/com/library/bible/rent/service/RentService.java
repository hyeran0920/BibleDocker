package com.library.bible.rent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.library.bible.rent.model.Rent;
import com.library.bible.rent.repository.IRentRepository;

import jakarta.transaction.Transactional;

@Service
public class RentService implements IRentService {
	
	@Autowired
	private IRentRepository rentRepository;

	@Override
	@Cacheable(value="rents")
	public List<Rent> selectAllRent() {
		return rentRepository.selectAllRent();
	}

	@Override
	@Cacheable(value="rent", key="#rentId")
	public Rent selectRent(int rentId) {
		return rentRepository.selectRent(rentId);
	}

	@Override
	@Transactional
	@CacheEvict(value = "rents", allEntries = true)
	public void insertRent(Rent rent) {
		rentRepository.insertRent(rent);
	}

	@Override
	@Transactional
	@CachePut(value = "rent", key = "#rent.rentId")
	public void updateRent(Rent rent) {
		rentRepository.updateRent(rent);
	}

	@Override
	@Transactional
	@Caching(evict= {
		@CacheEvict(value = "rent", key = "#rentId"),
		@CacheEvict(value = "rents", allEntries = true)
	})
	public int deleteRent(int rentId) {
		return rentRepository.deleteRent(rentId);

	}

}
