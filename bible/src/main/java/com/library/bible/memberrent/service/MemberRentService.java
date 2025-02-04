package com.library.bible.memberrent.service;

import org.springframework.stereotype.Service;

import com.library.bible.exception.CustomException;
import com.library.bible.exception.ExceptionCode;
import com.library.bible.memberrent.model.MemberRent;
import com.library.bible.memberrent.respository.IMemberRentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberRentService implements IMemberRentService {
	private final IMemberRentRepository memberRentRepository;

	// MemberRent
	@Override
	public MemberRent selectMemberRentByMemId(int memId) {
        return memberRentRepository.selectMemberRentByMemId(memId);
	}

	@Override
	@Transactional
	public int insertMemberRent(MemberRent memberRent) {
        int result = memberRentRepository.insertMemberRent(memberRent);
        if(result == 0) throw new CustomException(ExceptionCode.MEMBER_RENT_INSERT_FAIL);
        return result;
	}

	@Override
	@Transactional
	public int updateMemberRent(MemberRent memberRent) {
        int result = memberRentRepository.updateMemberRent(memberRent);
        if(result == 0) throw new CustomException(ExceptionCode.MEMBER_RENT_UPDATE_FAIL);
        return result;
	}

	@Override
	@Transactional
	public void deleteMemberRent(int memId) {
        int result = memberRentRepository.deleteMemberRent(memId);
        if(result == 0) throw new CustomException(ExceptionCode.MEMBER_RENT_DELETE_FAIL);
	}
}
