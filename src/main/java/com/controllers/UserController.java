package com.controllers;

import com.security.JWebToken;
import com.TokenManager;
import com.models.User;
import com.services.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity<HashMap> getUser(String token) throws JSONException {
        JWebToken tk = new TokenManager().check(token);
        if (tk == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Long id = Long.parseLong(tk.getSubject());
        HashMap user = userService.getUserById(id);
        HashMap res = new HashMap();
        res.put("response", user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<HashMap> registerUser(String firstName, String lastName, String login, String password) {
        HashMap init = userService.generateUserToken(login, password);
        if(init==null){
            User usr = new User();
            usr.setFirstName(firstName);
            usr.setLastName(lastName);
            usr.setLogin(login);
            usr.setPassword(password);
            usr.setId(userService.getNextId());
            usr.setType("client");
            userService.saveUser(usr);
            HashMap resp = new HashMap();
            HashMap token = userService.generateUserToken(login, password);
            resp.put("response", token);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }
    }

//    @GetMapping(value = "getById")
//    public ResponseEntity<HashMap> getById(String token, Long id) {
//        JWebToken tk = new TokenManager().check(token);
//        if (tk == null)
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//
//        HashMap usr = userService.getUserById(id);
//        if (usr != null) {
//            HashMap resp = new HashMap();
//            resp.put("response", usr);
//            return new ResponseEntity<>(resp, HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }

    @GetMapping(value = "auth")
    public ResponseEntity<HashMap> authUser(String login, String password) {
        HashMap data = userService.generateUserToken(login, password);
        if (data != null) {
            HashMap resp = new HashMap();
            resp.put("response", data);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
