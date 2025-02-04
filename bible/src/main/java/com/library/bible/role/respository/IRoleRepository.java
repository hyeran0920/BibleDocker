package com.library.bible.role.respository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.library.bible.member.model.Member;
import com.library.bible.role.model.Role;

@Mapper
@Repository
public interface IRoleRepository {
    List<Role> selectRolesByMemId(int memId);
    int insertMemberRoles(Member member); // member의 roles만 저장, 그 외의 컬럼은 저장하지 않음
    int insertRole(Role role);
    int deleteRoles(int memId);
}
