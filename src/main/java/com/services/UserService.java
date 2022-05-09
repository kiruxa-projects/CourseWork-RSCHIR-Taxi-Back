package com.services;

import com.JWebToken;
import com.models.User;
import com.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@Slf4j
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getUserById(Long id){
        return userRepository.findUserById(id);
    }

    @Transactional
    public String generateUserToken(String login, String password) {
        String jToken=null;
        User usr = userRepository.findUserByLoginAndPassword(login,password);

        try {
            JSONArray js = new JSONArray();
            js.put("client");
             jToken = new JWebToken(usr.getId().toString(),js, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)+1209600L).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jToken;
    }


}
