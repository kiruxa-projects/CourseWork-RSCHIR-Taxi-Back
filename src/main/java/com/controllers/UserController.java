package com.controllers;

import com.JWebToken;
import com.TokenManager;
import com.services.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        JWebToken tk= new TokenManager().check(token);
        if (tk ==null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Long id = Long.parseLong(tk.getSubject());
        HashMap user = userService.getUserById(id);
        HashMap res =new HashMap();
        res.put("response",user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "getById")
    public ResponseEntity<HashMap> getById(String token, Long id) {
        JWebToken tk= new TokenManager().check(token);
        if (tk ==null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

       HashMap usr = userService.getUserById(id);
        if (usr != null) {
            HashMap resp = new HashMap();
            resp.put("response",usr);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "getToken")
    public ResponseEntity<HashMap> getAllWorkers(String login, String password) {
        HashMap data = userService.generateUserToken(login, password);
        if (data != null) {
            HashMap resp = new HashMap();
            resp.put("response",data);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
