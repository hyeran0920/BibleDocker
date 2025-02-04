package com.library.bible.member.dto;

import java.util.List;

import com.library.bible.role.model.Role;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
	private int memId;
	private String memName;
	private String memPassword;
	
    @Valid
	private String memEmail;

	@Valid
	private String memPhone;
	
    private List<Role> roles;
}