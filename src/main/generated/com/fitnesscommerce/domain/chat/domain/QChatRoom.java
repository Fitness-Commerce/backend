package com.fitnesscommerce.domain.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -838360295L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final com.fitnesscommerce.domain.member.domain.QMember buyer;

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.fitnesscommerce.domain.item.domain.QItem item;

    public final StringPath roomName = createString("roomName");

    public final com.fitnesscommerce.domain.member.domain.QMember seller;

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new com.fitnesscommerce.domain.member.domain.QMember(forProperty("buyer"), inits.get("buyer")) : null;
        this.item = inits.isInitialized("item") ? new com.fitnesscommerce.domain.item.domain.QItem(forProperty("item"), inits.get("item")) : null;
        this.seller = inits.isInitialized("seller") ? new com.fitnesscommerce.domain.member.domain.QMember(forProperty("seller"), inits.get("seller")) : null;
    }

}

