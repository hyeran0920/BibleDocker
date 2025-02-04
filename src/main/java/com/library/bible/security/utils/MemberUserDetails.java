package com.library.bible.security.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.library.bible.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private final Member member;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return member.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority(role.getRoleName().toString()))
	            .collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return member.getMemPassword();
	}

	@Override
	public String getUsername() {
		return member.getMemEmail();
	} 

}
