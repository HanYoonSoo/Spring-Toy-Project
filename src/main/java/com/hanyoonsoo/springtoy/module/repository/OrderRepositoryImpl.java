package com.hanyoonsoo.springtoy.module.repository;

import com.hanyoonsoo.springtoy.module.entity.Order;
import com.hanyoonsoo.springtoy.module.entity.QOrder;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.hanyoonsoo.springtoy.module.entity.QDelivery.delivery;
import static com.hanyoonsoo.springtoy.module.entity.QOrder.order;
import static com.hanyoonsoo.springtoy.module.entity.QOrderItem.orderItem;
import static com.hanyoonsoo.springtoy.module.entity.QUser.user;
import static com.hanyoonsoo.springtoy.module.entity.item.QItem.item;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Order> findAllWithItem(OrderSearch orderSearch) {
        BooleanBuilder builder = new BooleanBuilder();

        orderSearchSQL(orderSearch, builder);

        return jpaQueryFactory
                .select(order)
                .distinct()
                .from(order)
                .join(order.user, user)
                .fetchJoin()
                .join(order.delivery, delivery)
                .fetchJoin()
                .join(order.orderItems, orderItem)
                .fetchJoin()
                .join(orderItem.item, item)
                .fetchJoin()
                .where(builder)
                .fetch();
    }


    @Override
    public List<Order> findAllWithItemPageable(Pageable pageable, String email) {
        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(email)){
            builder.and(user.email.eq(email));
        }
        return jpaQueryFactory
                .select(order)
                .from(order)
                .join(order.user, user)
                .fetchJoin()
                .join(order.delivery, delivery)
                .fetchJoin()
                .where(builder)
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private void orderSearchSQL(OrderSearch orderSearch, BooleanBuilder builder) {
        if(orderSearch.getOrderStatus() != null){
            builder.and(order.orderStatus.eq(orderSearch.getOrderStatus()));
        }

        if(StringUtils.hasText(orderSearch.getItemName())){
            builder.and(item.name.like(orderSearch.getItemName()));
        }

        if(StringUtils.hasText(orderSearch.getEmail())){
            builder.and(user.email.eq(orderSearch.getEmail()));
        }else{
            throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
