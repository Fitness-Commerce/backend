package com.fitnesscommerce.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSearch {

    private static final int MAX_SIZE = 2000;

    private Integer page;

    private Integer size;

    public long getOffset() {
        return (long) (Math.max(1,getPage()) - 1) * Math.min(getSize(), MAX_SIZE);
    }

    public Integer getPage() {
        return page != null ? page : 1;
    }

    public Integer getSize() {
        return size != null ? size : 10;
    }
}
