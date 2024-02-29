package com.hanyoonsoo.springtoy.module.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDelivery is a Querydsl query type for Delivery
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDelivery extends EntityPathBase<Delivery> {

    private static final long serialVersionUID = -1297706190L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDelivery delivery = new QDelivery("delivery");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QAddress address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> create_at = _super.create_at;

    public final BooleanPath deleted = createBoolean("deleted");

    public final EnumPath<DeliveryStatus> deliveryStatus = createEnum("deliveryStatus", DeliveryStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOrder order;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public QDelivery(String variable) {
        this(Delivery.class, forVariable(variable), INITS);
    }

    public QDelivery(Path<? extends Delivery> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDelivery(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDelivery(PathMetadata metadata, PathInits inits) {
        this(Delivery.class, metadata, inits);
    }

    public QDelivery(Class<? extends Delivery> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

