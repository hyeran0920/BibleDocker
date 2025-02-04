package com.library.bible.security.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.library.bible.member.model.Member;
import com.library.bible.member.service.IMemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
	@Autowired
	private final IMemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberService.selectMemberByMemEmail(username); // username(MemberUserDetails의 username) = email
		return new MemberUserDetails(member);
	}
	
	
}
