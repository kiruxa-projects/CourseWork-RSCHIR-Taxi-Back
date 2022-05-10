package com.services;

import com.JWebToken;
import com.models.User;
import com.repositories.CarRepository;
import com.repositories.OrderRepository;
import com.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
public class UserService {
    private UserRepository userRepository;
    private CarRepository carRepository;
    private OrderRepository orderRepository;

    @Autowired
    public UserService(UserRepository userRepository, CarRepository carRepository, OrderRepository orderRepository) {
        this.orderRepository=orderRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public HashMap getUserById(Long id) {
        HashMap result = userRepository.findUserById(id).toHashMap();
        if(result.get("type").equals("driver")){
            result.put("car",carRepository.findCarByDriverId(Long.parseLong(result.get("id").toString())));

            List orders = orderRepository.findOrderByDriverIdAndStatus(id,"active");
            if(orders.size()>0){
                result.put("activeOrder",orders.get(0));
            }
        }else{
            List orders = orderRepository.findOrderByClientIdAndStatus(id,"active");
            List orders2 = orderRepository.findOrderByClientIdAndStatus(id,"created");
            if(orders.size()>0){
                result.put("activeOrder",orders.get(0));
            }else if(orders2.size()>0){
                result.put("activeOrder",orders2.get(0));
            }
        }

        return result;
    }

    @Transactional
    public HashMap generateUserToken(String login, String password) {
        String jToken = null;
        User usr = userRepository.findUserByLoginAndPassword(login, password);
        if (usr == null)
            return null;
        try {
            JSONArray js = new JSONArray();
            js.put(usr.getType());
            jToken = new JWebToken(usr.getId().toString(), js, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 1209600L).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap res = new HashMap();
        HashMap usrHash =usr.toHashMap();
        res.put("token", jToken);
        if(usrHash.get("type").equals("driver")){
            usrHash.put("car",carRepository.findCarByDriverId(Long.parseLong(usrHash.get("id").toString())));
        }
        res.put("user", usrHash);
        return res;
    }


}
