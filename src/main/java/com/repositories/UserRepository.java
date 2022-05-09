package com.repositories;

import com.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);

    User findUserByLoginAndPassword(String Login, String Password);

//
//    @Query("SELECT User FROM User WHERE login = ?1 AND password = ?2")
//    User findUserByLoginAndPassword(String login, String password);

//    @Query("SELECT User FROM User WHERE firstName = ?1")
//    List<User> findUserByFirstNameCUSTOM(String firstName);
}
