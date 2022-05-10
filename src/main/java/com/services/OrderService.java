package com.services;

import com.models.Order;
import com.repositories.OrderRepository;
import com.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;


@Service
@Slf4j
public class OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private final SessionFactory sessionFactory;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, SessionFactory sessionFactory) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findOrderByClientIdAndStatus(userId, "completed");
    }

    @Transactional
    public Long getNextId(){
        if (orderRepository.getMaxId() == null) {
            return 0L;
        }
        return orderRepository.getMaxId() + 1;
    }

    @Transactional
    public boolean deleteOrder(Order ord){
        orderRepository.delete(ord);
        return true;
    }

    @Transactional
    public Order findOrderById(long id){return orderRepository.findOrderById(id);}

    @Transactional
    public List<Order> filter(String column, String pattern, Long userId, String userType) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteria = session.getCriteriaBuilder();
        CriteriaQuery<Order> orderCriteriaQuery = criteria.createQuery(Order.class);
        Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
        if(userType.equals("client")){
            orderCriteriaQuery.select(orderRoot).where(
                    criteria.and(criteria.equal(orderRoot.get(column), pattern), criteria.equal(orderRoot.get("clientId"), userId))
            );
        }else{
            System.out.println("ff");
            orderCriteriaQuery.select(orderRoot).where(
                    criteria.or(
                            criteria.and(criteria.equal(orderRoot.get(column), pattern), criteria.equal(orderRoot.get("driverId"), userId)),
                            criteria.and(criteria.equal(orderRoot.get(column), pattern), criteria.isNull(orderRoot.get("driverId")))
                    )
            );
        }

        Query query = session.createQuery(orderCriteriaQuery);
        return query.getResultList();
    }

    @Transactional
    public boolean saveOrder(Order ord){
        orderRepository.save(ord);
        return true;
    }

}
