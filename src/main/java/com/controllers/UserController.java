package com.controllers;

import com.JWebToken;
import com.models.User;
import com.services.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity<User> getUser(String token) throws JSONException, NoSuchAlgorithmException {
        if (token == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        JWebToken tk = new JWebToken(token);
        System.out.println(tk.isValid());
        if (!tk.isValid()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long id = Long.parseLong(tk.getSubject());
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "getToken")
    public ResponseEntity<HashMap> getAllWorkers(String login, String password) {
        String token = userService.generateUserToken(login, password);
        if (token != null) {
            HashMap resp = new HashMap();
            resp.put("token",token);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
