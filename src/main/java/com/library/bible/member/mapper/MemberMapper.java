package com.library.bible.member.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.library.bible.member.dto.MemberResponseDto;
import com.library.bible.member.model.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {
	MemberResponseDto memberToMemberResponseDto(Member member);
	List<MemberResponseDto> membersToMemberResponseDtos(List<Member> member);
}