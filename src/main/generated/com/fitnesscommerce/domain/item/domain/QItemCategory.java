package com.fitnesscommerce.domain.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemCategory is a Querydsl query type for ItemCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemCategory extends EntityPathBase<ItemCategory> {

    private static final long serialVersionUID = -1966388942L;

    public static final QItemCategory itemCategory = new QItemCategory("itemCategory");

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public QItemCategory(String variable) {
        super(ItemCategory.class, forVariable(variable));
    }

    public QItemCategory(Path<? extends ItemCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemCategory(PathMetadata metadata) {
        super(ItemCategory.class, metadata);
    }

}

