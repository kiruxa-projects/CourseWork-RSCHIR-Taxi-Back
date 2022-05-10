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
    private final SessionFactory factory;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, SessionFactory factory) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.factory = factory;
    }

    @Transactional
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findOrderByClientIdAndStatus(userId, "completed");
    }

    @Transactional
    public List<Order> filter(String column, String pattern, Long userId, String userType) {
        Session session = factory.openSession();
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


}
