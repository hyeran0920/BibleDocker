package com.library.bible.cart.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cart {
	private int cartId;
	private int bookId;
	private int memId;
	private int bookCount;
}
