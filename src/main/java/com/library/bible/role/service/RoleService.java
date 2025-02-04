package com.library.bible.role.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.library.bible.exception.CustomException;
import com.library.bible.exception.ExceptionCode;
import com.library.bible.member.model.Member;
import com.library.bible.role.model.Role;
import com.library.bible.role.respository.IRoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	private final IRoleRepository roleRepository;

	@Override
	@Transactional
	public void insertMemberRoles(Member member) {
		int result = roleRepository.insertMemberRoles(member);
    	if(result < 1) throw new CustomException(ExceptionCode.ROLE_INSERT_FAIL);
	}

	@Override
	@Transactional
	public Role insertRole(Role role) {
		int result = roleRepository.insertRole(role);
		if(result == 0)
			throw new CustomException(ExceptionCode.ROLE_INSERT_FAIL);
		return role;
	}
	
	@Override
	@Cacheable(value="role", key="#memId")
	public List<Role> selectRolesByMemId(int memId) {
		return roleRepository.selectRolesByMemId(memId);
	}

	@Override
	@Transactional
	public void deleteRoles(int memId) {
		int result = roleRepository.deleteRoles(memId);
		if(result == 0) throw new CustomException(ExceptionCode.ROLE_DELETE_FAIL);
	}
}
