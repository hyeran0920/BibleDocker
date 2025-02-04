package com.library.bible.address.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	private int addressId;
    private int memId;
    private String postcode;
	private String address;
	private String detailAddress;
}
