package com.controllers;

import com.JWebToken;
import com.TokenManager;
import com.models.Order;
import com.services.OrderService;
import com.services.UserService;
import org.aspectj.weaver.ast.Or;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    UserService userService;
    OrderService orderService;

    @Autowired
    public OrderController(UserService userService, OrderService orderService) {
        this.orderService=orderService;
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity<HashMap> getUser(String token) throws JSONException {
        JWebToken tk= new TokenManager().check(token);
        if (tk ==null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        HashMap res = new HashMap();
        res.put("result",orderService.getOrderHistory(Long.parseLong(tk.getSubject())));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("filter/{column}/{pattern}")
    public ResponseEntity<Map> filterWorkerWithPattern(@PathVariable("column") String column,@PathVariable("pattern") String pattern, String token) throws JSONException {
        JWebToken tk= new TokenManager().check(token);
        if (tk ==null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Map<String, Object[]> response = new HashMap<>();
        response.put("response", orderService.filter(column, pattern,Long.parseLong(tk.getSubject()),tk.getAudience().get(0)).toArray());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping(value = "")
//    public ResponseEntity<HashMap> createOrder(String token){
//
//
//    }
}
