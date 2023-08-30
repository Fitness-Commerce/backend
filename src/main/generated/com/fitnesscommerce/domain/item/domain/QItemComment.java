package com.fitnesscommerce.domain.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemComment is a Querydsl query type for ItemComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemComment extends EntityPathBase<ItemComment> {

    private static final long serialVersionUID = 608242603L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemComment itemComment = new QItemComment("itemComment");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final com.fitnesscommerce.domain.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public QItemComment(String variable) {
        this(ItemComment.class, forVariable(variable), INITS);
    }

    public QItemComment(Path<? extends ItemComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemComment(PathMetadata metadata, PathInits inits) {
        this(ItemComment.class, metadata, inits);
    }

    public QItemComment(Class<? extends ItemComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
        this.member = inits.isInitialized("member") ? new com.fitnesscommerce.domain.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

