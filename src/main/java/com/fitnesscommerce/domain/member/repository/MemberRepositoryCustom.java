package com.fitnesscommerce.domain.member.repository;

import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.dto.request.MemberSearch;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> getList(MemberSearch memberSearch);
}
