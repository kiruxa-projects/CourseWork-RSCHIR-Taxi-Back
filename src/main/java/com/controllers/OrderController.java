package com.controllers;

import com.JWebToken;
import com.TokenManager;
import com.models.Order;
import com.models.User;
import com.services.OrderService;
import com.services.UserService;
import org.aspectj.weaver.ast.Or;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    UserService userService;
    OrderService orderService;

    @Autowired
    public OrderController(UserService userService, OrderService orderService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity<HashMap> getUser(String token) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        HashMap res = new HashMap();
        res.put("response", orderService.getOrderHistory(Long.parseLong(tk.getSubject())));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("filter/{column}/{pattern}")
    public ResponseEntity<Map> filterWorkerWithPattern(@PathVariable("column") String column, @PathVariable("pattern") String pattern, String token) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Map<String, Object[]> response = new HashMap<>();
        response.put("response", orderService.filter(column, pattern, Long.parseLong(tk.getSubject()), tk.getAudience().get(0)).toArray());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    Я понимаю, что, если клиент будет сам задавать цену, то это очень небезопасно.
//    Но это сделано ради упрощения.

    @PostMapping(value = "")
    public ResponseEntity<HashMap> createOrder(String token, String fromAddress, String toAddress, Long cost, Long startTime, Long endTime, Long id) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        HashMap usr = userService.getUserById(Long.parseLong(tk.getSubject()));
        User usr1 = userService.getUserByIdAsUser(Long.parseLong(tk.getSubject()));
        HashMap res = new HashMap();
        if (usr.get("activeOrder") != null) {
            res.put("error", "The active order has not been completed yet");
            return new ResponseEntity<>(res, HttpStatus.CONFLICT);
        }
        if (usr.get("type").equals("client")) {
            Order ord = new Order();
            ord.setCost(cost);
            ord.setClientId(Long.parseLong(tk.getSubject()));
            ord.setToAddress(toAddress);
            ord.setFromAddress(fromAddress);
            ord.setId(orderService.getNextId());
            ord.setStatus("created");
            orderService.saveOrder(ord);
            usr1.setActiveOrder(ord.getId());
            userService.saveUser(usr1);
            res.put("response", ord);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } else if (usr.get("type").equals("driver")) {
            Order ord = orderService.findOrderById(id);
            if (ord == null) {
                res.put("error", "Order not found");
                return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
            }

            ord.setDriverId(Long.parseLong(tk.getSubject()));
            ord.setStatus("active");
            ord.setStartTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            orderService.saveOrder(ord);
            res.put("result", ord);
            usr1.setActiveOrder(ord.getId());
            userService.saveUser(usr1);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "")
    public ResponseEntity<HashMap> deleteOrder(String token) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (tk.getAudience().get(0).equals("client")) {
            User usr = userService.getUserByIdAsUser(Long.parseLong(tk.getSubject()));
            HashMap usrHash = userService.getUserById(Long.parseLong(tk.getSubject()));
            Order orderObj = (Order) usrHash.get("activeOrder");

            if(orderObj==null)
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

            Order order = orderService.findOrderById(orderObj.getId());
            usr.setActiveOrder(null);
            if(order.getDriverId()!=null){
                User driver = userService.getUserByIdAsUser(order.getDriverId());
                driver.setActiveOrder(null);
                userService.saveUser(driver);
            }
            userService.saveUser(usr);
            orderService.deleteOrder(order);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
}