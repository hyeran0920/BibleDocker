package com.library.bible.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.bible.address.model.Address;
import com.library.bible.address.respository.IAddressRepository;
import com.library.bible.exception.CustomException;
import com.library.bible.exception.ExceptionCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
	private final IAddressRepository addressRepository;

	@Override
	public List<Address> selectAddressesByMemId(int memId) {
		return addressRepository.selectAddressesByMemId(memId);
	}

	@Override
	public Address seleAddress(int addressId) {
        return addressRepository.seleAddress(addressId);
	}

	@Override
	@Transactional
	public Address insertAddress(Address address) {
        int result = addressRepository.insertAddress(address);
        if(result == 0) throw new CustomException(ExceptionCode.ADDRESS_INSERT_FAIL);
        return address;
	}

	@Override
	@Transactional
	public int updateAddress(Address address) {
		Address bAddress = this.seleAddress(address.getAddressId());
		if(bAddress.getMemId() != address.getMemId())
			throw new CustomException(ExceptionCode.FORBIDDEN);
		
		System.out.println(address);

		
		
        int result = addressRepository.updateAddress(address);
        if(result == 0) throw new CustomException(ExceptionCode.ADDRESS_UPDATE_FAIL);
        return result;
	}

	@Override
	@Transactional
	public void deleteAddressesByMemId(int memId) {
        addressRepository.deleteAddressesByMemId(memId);
	}

	@Override
	@Transactional
	public void deleteAddress(int addressId) {
		int result = addressRepository.deleteAddress(addressId);
        if(result == 0) throw new CustomException(ExceptionCode.ADDRESS_DELETE_FAIL);
	}
}
