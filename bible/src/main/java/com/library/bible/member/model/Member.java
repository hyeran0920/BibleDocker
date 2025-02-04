package com.library.bible.member.model;

import java.util.List;

import com.library.bible.role.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	private int memId;
	private String memName;
	private String memPassword;
	
    @Email(message = "유효한 이메일 형식이 아닙니다.")
	private String memEmail;

	@Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (010-XXXX-XXXX)")
	private String memPhone;
	
    private List<Role> roles;
}