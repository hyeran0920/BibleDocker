package com.library.bible.address.service;

import java.util.List;

import com.library.bible.address.model.Address;

public interface IAddressService {
    List<Address> selectAddressesByMemId(int memId);
    Address seleAddress(int addressId);
    Address insertAddress(Address address);
    int updateAddress(Address address);
    void deleteAddressesByMemId(int memId);
    void deleteAddress(int addressId);    
}
