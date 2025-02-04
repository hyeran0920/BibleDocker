package com.library.bible.member.dto;

import java.util.List;

import com.library.bible.role.model.Role;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
	private int memId;
	private String memName;
    @Valid
	private String memEmail;
    @Valid
	private String memPhone;
    private List<Role> roles;
}