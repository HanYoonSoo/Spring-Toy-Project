//package com.hanyoonsoo.springtoy;
//
//import com.hanyoonsoo.springtoy.module.constants.Authority;
//import com.hanyoonsoo.springtoy.module.entity.*;
//import com.hanyoonsoo.springtoy.module.entity.item.Book;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//
///**
// * 총 주문 2개
// * * userA
// *  * JPA1 BOOK
// *  * JPA2 BOOK
// * * userB
// *  * SPRING1 BOOK
// *  * SPRING2 BOOk
// * */
//@Component
//@RequiredArgsConstructor
//public class InitDb {
//
//    private final InitService initService;
//
//    @PostConstruct
//    public void init(){
////        initService.dbInit1();
////        initService.dbInit2();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService{
//
//        private final EntityManager em;
//        public void dbInit1(){
//            User user1= createUser("email@gmail.com", "1234", "test1", "test1", new Address("test1", "1234", "1234"), Gender.from("남자"));
//            em.persist(user1);
//
//            Book book1 = createBook("JPA1 BOOK", 10000, 100);
//            em.persist(book1);
//
//            Book book2 = createBook("JPA2 BOOK", 20000, 100);
//            em.persist(book2);
//
//            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
//            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 1);
//
//            Delivery delivery = createDelivery(user1);
//
//            Order order = Order.createOrder(user1, delivery, orderItem1, orderItem2);
//            em.persist(order);
//        }
//
//        private Delivery createDelivery(User user) {
//            Delivery delivery = new Delivery();
//            delivery.setAddress(user.getAddress());
//            return delivery;
//        }
//
//        private Book createBook(String name, int price, int stockQuantity) {
//            Book book1 = new Book();
//            book1.setName(name);
//            book1.setPrice(price);
//            book1.setStockQuantity(stockQuantity);
//            return book1;
//        }
//
//        public void dbInit2(){
//            User user2 = createUser("email2@gmail.com", "1234", "test2", "test2", new Address("test2", "1234", "1234"), Gender.from("여자"));
//            em.persist(user2);
//
//            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
//            em.persist(book1);
//
//            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
//            em.persist(book2);
//
//            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 1);
//            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 1);
//
//            Delivery delivery = createDelivery(user2);
//
//            Order order = Order.createOrder(user2, delivery, orderItem1, orderItem2);
//            em.persist(order);
//        }
//
//        private static User createUser(String mail, String password, String name, String nickname, Address address, Gender gender) {
//            User user = new User();
//            user.setEmail(mail);
//            user.setPassword(password);
//            user.setName(name);
//            user.setNickName(nickname);
//            user.setAddress(address);
//            user.setGender(gender);
//            user.setRole(Authority.ROLE_VERIFIED_USER);
//            return user;
//        }
//    }
//}
