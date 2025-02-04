package com.library.bible.rent.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentHistory {
	private int rentHistoryId;
	private int memId;
	private Date rentDate;
}
