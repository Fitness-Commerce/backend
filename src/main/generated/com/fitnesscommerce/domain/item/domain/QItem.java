package com.fitnesscommerce.domain.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 701594644L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItemCategory itemCategory;

    public final StringPath itemDetail = createString("itemDetail");

    public final ListPath<ItemImage, QItemImage> itemImages = this.<ItemImage, QItemImage>createList("itemImages", ItemImage.class, QItemImage.class, PathInits.DIRECT2);

    public final StringPath itemName = createString("itemName");

    public final NumberPath<Integer> itemPrice = createNumber("itemPrice", Integer.class);

    public final EnumPath<ItemStatus> itemStatus = createEnum("itemStatus", ItemStatus.class);

    public final com.fitnesscommerce.domain.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.itemCategory = inits.isInitialized("itemCategory") ? new QItemCategory(forProperty("itemCategory")) : null;
        this.member = inits.isInitialized("member") ? new com.fitnesscommerce.domain.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

