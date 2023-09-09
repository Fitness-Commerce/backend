package com.fitnesscommerce.domain.member.repository;

import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.domain.QMember;
import com.fitnesscommerce.domain.member.dto.request.MemberSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Member> getList(MemberSearch memberSearch) {
        return jpaQueryFactory.selectFrom(QMember.member)
                .limit(memberSearch.getSize())
                .offset(memberSearch.getOffset())
                .orderBy(QMember.member.id.desc())
                .fetch();
    }
}
