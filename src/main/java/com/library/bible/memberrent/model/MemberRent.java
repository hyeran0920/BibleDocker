package com.library.bible.memberrent.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRent {
    private int memId;
    private int totalRentCount;
    private char rentPoss; 		// t: 대여가능, f: 대여불가능
    private Date rentPossDate;	// 대여 가능한 날짜(null이면 항상 대여 가능함)
}
