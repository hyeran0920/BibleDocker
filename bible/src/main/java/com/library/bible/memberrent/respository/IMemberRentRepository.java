package com.library.bible.memberrent.respository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.library.bible.memberrent.model.MemberRent;

@Mapper
@Repository
public interface IMemberRentRepository {
    MemberRent selectMemberRentByMemId(int memId);
    int insertMemberRent(MemberRent memberRent);
    int updateMemberRent(MemberRent memberRent);
    int deleteMemberRent(int memId);
}
