package com.library.bible.rent.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rent {
	private int rentId;
	private int bookId;
	private int rentHistoryId;
	private Date rentDueDate;
	private Date rentFinishDate;
	private String rentStatus;
}
